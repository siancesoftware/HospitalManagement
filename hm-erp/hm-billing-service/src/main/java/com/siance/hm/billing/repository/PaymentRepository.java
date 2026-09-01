package com.siance.hm.billing.repository;

import com.siance.hm.billing.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByIdAndHospitalId(UUID id, UUID hospitalId);
    Page<Payment> findByHospitalId(UUID hospitalId, Pageable pageable);
    List<Payment> findByHospitalIdAndPatientId(UUID hospitalId, UUID patientId);
    List<Payment> findByVisitId(UUID visitId);
}
