package com.siance.hm.lab.repository;

import com.siance.hm.lab.entity.LabTestPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LabTestPaymentRepository extends JpaRepository<LabTestPayment, UUID> {
    List<LabTestPayment> findByBillId(UUID billId);
}
