package com.siance.hm.opd.service;

import com.siance.hm.common.exception.ResourceNotFoundException;
import com.siance.hm.common.response.PagedResult;
import com.siance.hm.common.web.PageQuery;
import com.siance.hm.opd.dto.CreateVisitRequest;
import com.siance.hm.opd.dto.VisitResponse;
import com.siance.hm.opd.entity.Visit;
import com.siance.hm.opd.entity.VisitStatus;
import com.siance.hm.opd.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

/**
 * Port of the original opd-service visit module:
 * creation + daily token numbering + status lifecycle.
 */
@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;

    @Override
    @Transactional
    public VisitResponse create(
            CreateVisitRequest request,
            UUID hospitalId
    ) {
        Visit visit = new Visit();

        visit.setPatientId(request.getPatientId());
        visit.setHospitalId(hospitalId);
        visit.setDepartmentId(request.getDepartmentId());
        visit.setDoctorId(request.getDoctorId());
        visit.setVisitType(request.getVisitType());
        visit.setChiefComplaint(request.getChiefComplaint());
        visit.setStatus(VisitStatus.WAITING);
        visit.setTokenNumber(nextTokenNumber(hospitalId));
        visit.setStartedAt(OffsetDateTime.now());

        return toResponse(
                visitRepository.save(visit)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public VisitResponse getById(
            UUID id,
            UUID hospitalId
    ) {
        return toResponse(
                find(id, hospitalId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResult<VisitResponse> list(
            PageQuery query,
            UUID hospitalId
    ) {
        Page<Visit> page =
                visitRepository.findByHospitalId(
                        hospitalId,
                        query.toPageable("createdAt")
                );

        List<VisitResponse> data =
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
    public List<VisitResponse> forPatient(
            UUID patientId,
            UUID hospitalId
    ) {
        return visitRepository
                .findByHospitalIdAndPatientId(
                        hospitalId,
                        patientId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public VisitResponse updateStatus(
            UUID id,
            VisitStatus status,
            UUID hospitalId
    ) {
        Visit visit = find(id, hospitalId);

        visit.setStatus(status);

        if (status == VisitStatus.COMPLETED
                || status == VisitStatus.CANCELLED) {
            visit.setEndedAt(OffsetDateTime.now());
        }

        return toResponse(
                visitRepository.save(visit)
        );
    }

    private Visit find(
            UUID id,
            UUID hospitalId
    ) {
        return visitRepository
                .findByIdAndHospitalId(id, hospitalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Visit not found with id: " + id
                        )
                );
    }

    /**
     * Simplified per-hospital daily counter.
     * Original resets per department per day.
     */
    private int nextTokenNumber(UUID hospitalId) {
        return (int) visitRepository.countByHospitalId(hospitalId) + 1;
    }

    private VisitResponse toResponse(Visit v) {
        return new VisitResponse(
                v.getId(),
                v.getPatientId(),
                v.getHospitalId(),
                v.getVisitType().name(),
                v.getDepartmentId(),
                v.getDoctorId(),
                v.getStatus().name(),
                v.getTokenNumber(),
                v.getChiefComplaint(),
                v.getStartedAt(),
                v.getEndedAt(),
                v.getCreatedAt().atOffset(ZoneOffset.UTC)
        );
    }
}