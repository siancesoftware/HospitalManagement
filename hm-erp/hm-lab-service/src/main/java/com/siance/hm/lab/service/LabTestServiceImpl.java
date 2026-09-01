package com.siance.hm.lab.service.impl;

import com.siance.hm.common.exception.ResourceNotFoundException;
import com.siance.hm.lab.dto.LabTestRequest;
import com.siance.hm.lab.dto.LabTestResponse;
import com.siance.hm.lab.entity.LabTest;
import com.siance.hm.lab.repository.LabTestRepository;
import com.siance.hm.lab.service.LabTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Port of the original test-master catalog module.
 */
@Service
@RequiredArgsConstructor
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository labTestRepository;

    @Override
    @Transactional
    public LabTestResponse create(
            LabTestRequest request,
            UUID hospitalId
    ) {
        LabTest test = new LabTest();

        apply(request, test);
        test.setHospitalId(hospitalId);

        return toResponse(
                labTestRepository.save(test)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestResponse> search(
            String query,
            UUID hospitalId
    ) {
        List<LabTest> tests =
                (query == null || query.isBlank())
                        ? labTestRepository
                                .findByHospitalIdAndActiveTrue(hospitalId)
                        : labTestRepository
                                .findByHospitalIdAndNameContainingIgnoreCase(
                                        hospitalId,
                                        query
                                );

        return tests.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public LabTestResponse update(
            UUID id,
            LabTestRequest request,
            UUID hospitalId
    ) {
        LabTest test =
                labTestRepository
                        .findByIdAndHospitalId(id, hospitalId)
                        .orElseThrow(() ->
                                ResourceNotFoundException.of(
                                        "LabTest",
                                        id
                                )
                        );

        apply(request, test);

        return toResponse(
                labTestRepository.save(test)
        );
    }

    private void apply(
            LabTestRequest request,
            LabTest test
    ) {
        test.setTestCode(request.getTestCode());
        test.setName(request.getName());
        test.setLoincCode(request.getLoincCode());
        test.setSection(request.getSection());
        test.setSampleType(request.getSampleType());
        test.setTatHours(request.getTatHours());
        test.setPrice(request.getPrice());
    }

    private LabTestResponse toResponse(LabTest t) {
        return new LabTestResponse(
                t.getId(),
                t.getTestCode(),
                t.getName(),
                t.getLoincCode(),
                t.getSection(),
                t.getSampleType(),
                t.getTatHours(),
                t.getPrice(),
                t.isActive()
        );
    }
}