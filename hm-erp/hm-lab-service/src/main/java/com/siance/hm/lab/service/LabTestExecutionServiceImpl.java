package com.siance.hm.lab.service.impl;

import com.siance.hm.common.exception.ConflictException;
import com.siance.hm.common.exception.ResourceNotFoundException;
import com.siance.hm.common.response.PagedResult;
import com.siance.hm.common.web.PageQuery;
import com.siance.hm.lab.dto.LabTestExecutionResponse;
import com.siance.hm.lab.dto.OrderLabTestRequest;
import com.siance.hm.lab.entity.Lab;
import com.siance.hm.lab.entity.LabTest;
import com.siance.hm.lab.entity.LabTestExecution;
import com.siance.hm.lab.entity.LabTestExecutionStatus;
import com.siance.hm.lab.repository.LabRepository;
import com.siance.hm.lab.repository.LabTestExecutionRepository;
import com.siance.hm.lab.repository.LabTestRepository;
import com.siance.hm.lab.service.LabTestExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Port of the LIS order/sample/result/validate workflow (original
 * hm-lab-service test-execution + critical-alert modules, minus the
 * instrument interfacing and critical-value Kafka alert).
 */
@Service
@RequiredArgsConstructor
public class LabTestExecutionServiceImpl implements LabTestExecutionService {

    private final LabTestExecutionRepository executionRepository;
    private final LabRepository labRepository;
    private final LabTestRepository labTestRepository;

    @Override
    @Transactional
    public LabTestExecutionResponse order(
            OrderLabTestRequest request,
            UUID orderedBy,
            UUID hospitalId
    ) {
        Lab lab = labRepository
                .findByIdAndHospitalId(request.getLabId(), hospitalId)
                .orElseThrow(() ->
                        ResourceNotFoundException.of(
                                "Lab",
                                request.getLabId()
                        )
                );

        LabTest test = labTestRepository
                .findByIdAndHospitalId(request.getTestId(), hospitalId)
                .orElseThrow(() ->
                        ResourceNotFoundException.of(
                                "LabTest",
                                request.getTestId()
                        )
                );

        LabTestExecution execution = new LabTestExecution();
        execution.setLab(lab);
        execution.setTest(test);
        execution.setHospitalId(hospitalId);
        execution.setPatientId(request.getPatientId());
        execution.setVisitId(request.getVisitId());
        execution.setPrescriptionLabTestRef(
                request.getPrescriptionLabTestRef()
        );
        execution.setOrderedBy(orderedBy);
        execution.setStatus(LabTestExecutionStatus.ORDERED);

        return toResponse(
                executionRepository.save(execution)
        );
    }

    @Override
    @Transactional
    public LabTestExecutionResponse collectSample(
            UUID id,
            String barcode,
            UUID hospitalId
    ) {
        LabTestExecution execution = find(id, hospitalId);

        requireStatus(
                execution,
                LabTestExecutionStatus.ORDERED
        );

        execution.setSampleBarcode(barcode);
        execution.setSampleCollectedAt(OffsetDateTime.now());
        execution.setStatus(
                LabTestExecutionStatus.SAMPLE_COLLECTED
        );

        return toResponse(
                executionRepository.save(execution)
        );
    }

    @Override
    @Transactional
    public LabTestExecutionResponse enterResult(
            UUID id,
            String resultValuesJson,
            UUID hospitalId
    ) {
        LabTestExecution execution = find(id, hospitalId);

        if (execution.getStatus() == LabTestExecutionStatus.VALIDATED
                || execution.getStatus() == LabTestExecutionStatus.CANCELLED) {

            throw new ConflictException(
                    "Cannot enter results on a "
                            + execution.getStatus()
                            + " order."
            );
        }

        execution.setResultValuesJson(resultValuesJson);
        execution.setResultEnteredAt(OffsetDateTime.now());
        execution.setStatus(
                LabTestExecutionStatus.RESULT_ENTERED
        );

        return toResponse(
                executionRepository.save(execution)
        );
    }

    @Override
    @Transactional
    public LabTestExecutionResponse validate(
            UUID id,
            UUID validatedBy,
            UUID hospitalId
    ) {
        LabTestExecution execution = find(id, hospitalId);

        requireStatus(
                execution,
                LabTestExecutionStatus.RESULT_ENTERED
        );

        execution.setValidatedBy(validatedBy);
        execution.setValidatedAt(OffsetDateTime.now());
        execution.setStatus(
                LabTestExecutionStatus.VALIDATED
        );

        return toResponse(
                executionRepository.save(execution)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public LabTestExecutionResponse getById(
            UUID id,
            UUID hospitalId
    ) {
        return toResponse(
                find(id, hospitalId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResult<LabTestExecutionResponse> list(
            PageQuery query,
            UUID hospitalId
    ) {
        Page<LabTestExecution> page =
                executionRepository.findByHospitalId(
                        hospitalId,
                        query.toPageable("createdAt")
                );

        List<LabTestExecutionResponse> data =
                page.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return PagedResult.fromZeroBasedPage(
                data,
                page.getTotalElements(),
                page.getNumber(),
                query.getLimit()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestExecutionResponse> forPatient(
            UUID patientId,
            UUID hospitalId
    ) {
        return executionRepository
                .findByHospitalIdAndPatientId(
                        hospitalId,
                        patientId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void requireStatus(
            LabTestExecution execution,
            LabTestExecutionStatus expected
    ) {
        if (execution.getStatus() != expected) {
            throw new ConflictException(
                    "Expected status "
                            + expected
                            + " but order is "
                            + execution.getStatus()
            );
        }
    }

    private LabTestExecution find(
            UUID id,
            UUID hospitalId
    ) {
        return executionRepository
                .findByIdAndHospitalId(id, hospitalId)
                .orElseThrow(() ->
                        ResourceNotFoundException.of(
                                "LabTestExecution",
                                id
                        )
                );
    }

    private LabTestExecutionResponse toResponse(
            LabTestExecution e
    ) {
        return new LabTestExecutionResponse(
                e.getId(),
                e.getLab().getId(),
                e.getTest().getId(),
                e.getTest().getName(),
                e.getPatientId(),
                e.getVisitId(),
                e.getStatus().name(),
                e.getSampleBarcode(),
                e.getSampleCollectedAt(),
                e.getResultValuesJson(),
                e.getResultEnteredAt(),
                e.getValidatedAt(),
                e.getReportUrl()
        );
    }
}