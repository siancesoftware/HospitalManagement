package com.siance.hm.lab.repository;

import com.siance.hm.lab.entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LabTestRepository extends JpaRepository<LabTest, UUID> {
    List<LabTest> findByHospitalIdAndActiveTrue(UUID hospitalId);
    Optional<LabTest> findByIdAndHospitalId(UUID id, UUID hospitalId);
    List<LabTest> findByHospitalIdAndNameContainingIgnoreCase(UUID hospitalId, String name);
}
