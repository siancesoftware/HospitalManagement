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
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;

    @Override
    @Transactional
    public ConsultationResponse createOrUpdate(
            ConsultationRequest request,
            UUID doctorId,
            UUID hospitalId
    ) {
        Consultation consultation = consultationRepository
                .findByVisitIdAndHospitalId(
                        request.getVisitId(),
                        hospitalId
                )
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

        Consultation savedConsultation =
                consultationRepository.save(consultation);

        return toResponse(savedConsultation);
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultationResponse getByVisit(
            UUID visitId,
            UUID hospitalId
    ) {
        return consultationRepository
                .findByVisitIdAndHospitalId(visitId, hospitalId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation for visit not found: " + visitId
                ));
    }

    private ConsultationResponse toResponse(Consultation consultation) {
        return new ConsultationResponse(
                consultation.getId(),
                consultation.getVisitId(),
                consultation.getPatientId(),
                consultation.getDoctorId(),
                consultation.getSubjective(),
                consultation.getObjective(),
                consultation.getAssessment(),
                consultation.getPlan(),
                consultation.getDiagnosis()
        );
    }
}