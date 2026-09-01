package com.siance.hm.opd.repository;

import com.siance.hm.opd.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {
    Optional<Prescription> findByIdAndHospitalId(UUID id, UUID hospitalId);
    List<Prescription> findByVisitIdAndHospitalId(UUID visitId, UUID hospitalId);
}
