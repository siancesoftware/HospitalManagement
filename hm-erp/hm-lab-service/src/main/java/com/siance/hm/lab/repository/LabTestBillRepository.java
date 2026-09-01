package com.siance.hm.lab.repository;

import com.siance.hm.lab.entity.LabTestBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LabTestBillRepository extends JpaRepository<LabTestBill, UUID> {
    Optional<LabTestBill> findByIdAndHospitalId(UUID id, UUID hospitalId);
    long countByHospitalId(UUID hospitalId);
}
