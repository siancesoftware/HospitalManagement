package com.siance.hm.opd.repository;

import com.siance.hm.opd.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitRepository extends JpaRepository<Visit, UUID> {
    Optional<Visit> findByIdAndHospitalId(UUID id, UUID hospitalId);
    Page<Visit> findByHospitalId(UUID hospitalId, Pageable pageable);
    List<Visit> findByHospitalIdAndDoctorId(UUID hospitalId, UUID doctorId);
    List<Visit> findByHospitalIdAndPatientId(UUID hospitalId, UUID patientId);
    long countByHospitalId(UUID hospitalId);
}
