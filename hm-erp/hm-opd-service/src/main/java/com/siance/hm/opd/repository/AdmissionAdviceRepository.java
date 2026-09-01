package com.siance.hm.opd.repository;

import com.siance.hm.opd.entity.AdmissionAdvice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdmissionAdviceRepository extends JpaRepository<AdmissionAdvice, UUID> {
    List<AdmissionAdvice> findByAdmissionId(UUID admissionId);
}
