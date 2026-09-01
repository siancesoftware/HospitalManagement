package com.siance.hm.lab.repository;

import com.siance.hm.lab.entity.Lab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LabRepository extends JpaRepository<Lab, UUID> {
    List<Lab> findByHospitalId(UUID hospitalId);
    Optional<Lab> findByIdAndHospitalId(UUID id, UUID hospitalId);
}
