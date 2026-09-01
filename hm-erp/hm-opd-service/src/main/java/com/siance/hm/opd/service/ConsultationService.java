package com.siance.hm.opd.service;

import com.siance.hm.common.exception.ResourceNotFoundException;
import com.siance.hm.opd.dto.ConsultationRequest;
import com.siance.hm.opd.dto.ConsultationResponse;
import com.siance.hm.opd.entity.Consultation;
import com.siance.hm.opd.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    @Transactional
    public ConsultationResponse createOrUpdate(ConsultationRequest request, UUID doctorId, UUID hospitalId) {
        Consultation consultation = consultationRepository
                .findByVisitIdAndHospitalId(request.getVisitId(), hospitalId)
                .orElseGet(Consultation::new);

        consultation.setVisitId(request.getVisitId());
        consultation.setPatientId(request.getPatientId());
        consultation.setDoctorId(doctorId);
        consultation.setHospitalId(hospitalId);
        consultation.setSubjective(request.getSubjective());
        consultation.setObjective(request.getObjective());
        consultation.setAssessment(request.getAssessment());
        consultation.setPlan(request.getPlan());
        consultation.setDiagnosis(request.getDiagnosis());

        return toResponse(consultationRepository.save(consultation));
    }

    @Transactional(readOnly = true)
    public ConsultationResponse getByVisit(UUID visitId, UUID hospitalId) {
        return consultationRepository.findByVisitIdAndHospitalId(visitId, hospitalId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation for visit not found: " + visitId
                ));
    }

    private ConsultationResponse toResponse(Consultation c) {
        return new ConsultationResponse(c.getId(), c.getVisitId(), c.getPatientId(), c.getDoctorId(),
                c.getSubjective(), c.getObjective(), c.getAssessment(), c.getPlan(), c.getDiagnosis());
    }
}
