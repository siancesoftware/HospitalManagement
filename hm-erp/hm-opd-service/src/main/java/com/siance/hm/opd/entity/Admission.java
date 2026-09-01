package com.siance.hm.opd.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/** Port of {@code Admission}. bedId/wardId reference hm-hospital-service. */
@Entity
@Table(name = "admissions", indexes = @Index(name = "idx_admissions_patient_id", columnList = "patient_id"))
@Getter
@Setter
@NoArgsConstructor
public class Admission extends BaseEntity {

    @Column(name = "admission_number", nullable = false, unique = true, length = 30)
    private String admissionNumber;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "hospital_id", nullable = false)
    private UUID hospitalId;

    @Column(name = "visit_id")
    private UUID visitId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdmissionType type = AdmissionType.PLANNED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdmissionPriority priority = AdmissionPriority.ROUTINE;

    @Column(name = "admitting_doctor_id")
    private UUID admittingDoctorId;

    @Column(name = "primary_consultant_id")
    private UUID primaryConsultantId;

    @Column(name = "bed_id")
    private UUID bedId;

    @Column(name = "ward_id")
    private UUID wardId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdmissionStatus status = AdmissionStatus.ADMITTED;

    @Column(name = "admitted_at", nullable = false)
    private OffsetDateTime admittedAt = OffsetDateTime.now();

    @Column(name = "discharged_at")
    private OffsetDateTime dischargedAt;

    @Column(name = "estimated_cost", precision = 12, scale = 2)
    private BigDecimal estimatedCost;

    @Column(name = "discharge_summary", columnDefinition = "text")
    private String dischargeSummary;
}
