package com.siance.hm.lab.repository;

import com.siance.hm.lab.entity.LabStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LabStaffRepository extends JpaRepository<LabStaff, UUID> {
    List<LabStaff> findByHospitalUserIdAndActiveTrue(UUID hospitalUserId);
    boolean existsByLabIdAndHospitalUserIdAndActiveTrue(UUID labId, UUID hospitalUserId);
}
