package com.siance.hm.patient.repository;

import com.siance.hm.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID>, JpaSpecificationExecutor<Patient> {

    Optional<Patient> findByUhidAndDeletedAtIsNull(String uhid);

    Optional<Patient> findByNationalIdAndDeletedAtIsNull(String nationalId);

    boolean existsByUhid(String uhid);

    boolean existsByNationalIdAndDeletedAtIsNull(String nationalId);

    @Query("SELECT p FROM Patient p WHERE p.deletedAt IS NULL AND " +
           "(LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Patient> searchByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.deletedAt IS NULL AND p.primaryPhone LIKE CONCAT('%', :phone, '%')")
    Page<Patient> searchByPhone(@Param("phone") String phone, Pageable pageable);

    @Query(value = "SELECT nextval('uhid_sequence')", nativeQuery = true)
    Long getNextUhidSequence();
}
