package com.siance.hm.opd.repository;

import com.siance.hm.opd.entity.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProcedureRepository extends JpaRepository<Procedure, UUID> {
    List<Procedure> findByHospitalId(UUID hospitalId);
}
