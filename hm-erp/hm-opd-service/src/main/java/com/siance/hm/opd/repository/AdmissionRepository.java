package com.siance.hm.opd.repository;

import com.siance.hm.opd.entity.Admission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdmissionRepository extends JpaRepository<Admission, UUID> {
    Optional<Admission> findByIdAndHospitalId(UUID id, UUID hospitalId);
    Page<Admission> findByHospitalId(UUID hospitalId, Pageable pageable);
    long countByHospitalId(UUID hospitalId);
}
