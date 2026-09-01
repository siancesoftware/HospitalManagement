package com.siance.hm.opd.repository;

import com.siance.hm.opd.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
    Optional<Consultation> findByVisitIdAndHospitalId(UUID visitId, UUID hospitalId);
    List<Consultation> findByPatientIdAndHospitalId(UUID patientId, UUID hospitalId);
}
