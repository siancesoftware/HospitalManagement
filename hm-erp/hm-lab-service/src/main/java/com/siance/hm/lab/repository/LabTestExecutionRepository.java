package com.siance.hm.lab.repository;

import com.siance.hm.lab.entity.LabTestExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LabTestExecutionRepository extends JpaRepository<LabTestExecution, UUID> {
    Optional<LabTestExecution> findByIdAndHospitalId(UUID id, UUID hospitalId);
    Page<LabTestExecution> findByHospitalId(UUID hospitalId, Pageable pageable);
    List<LabTestExecution> findByHospitalIdAndPatientId(UUID hospitalId, UUID patientId);
}
