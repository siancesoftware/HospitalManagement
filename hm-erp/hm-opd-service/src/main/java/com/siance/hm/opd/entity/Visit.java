package com.siance.hm.opd.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Port of the {@code Visit} Prisma model. {@code patientId}/{@code doctorId}
 * reference hm-patient-service / hm-hospital-service respectively - plain
 * UUID columns, resolved via REST when a display name is needed (see
 * {@code hm-opd-service} README section on inter-service reads).
 */
@Entity
@Table(name = "visits", indexes = {
        @Index(name = "idx_visits_hospital_id", columnList = "hospital_id"),
        @Index(name = "idx_visits_patient_id", columnList = "patient_id"),
        @Index(name = "idx_visits_doctor_id", columnList = "doctor_id")
})
@Getter
@Setter
@NoArgsConstructor
public class Visit extends BaseEntity {

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "hospital_id", nullable = false)
    private UUID hospitalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_type", nullable = false, length = 20)
    private VisitType visitType = VisitType.OPD;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VisitStatus status = VisitStatus.SCHEDULED;

    @Column(name = "token_number")
    private Integer tokenNumber;

    @Column(name = "chief_complaint", columnDefinition = "text")
    private String chiefComplaint;

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;
}
