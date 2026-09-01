package com.siance.hm.lab.repository;

import com.siance.hm.lab.entity.LabToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LabTokenRepository extends JpaRepository<LabToken, UUID> {
    long countByLabIdAndHospitalId(UUID labId, UUID hospitalId);
}
