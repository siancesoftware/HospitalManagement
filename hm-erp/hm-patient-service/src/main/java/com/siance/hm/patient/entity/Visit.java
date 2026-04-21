package com.siance.hm.patient.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "visits", indexes = {
    @Index(name = "idx_visit_patient", columnList = "patient_id"),
    @Index(name = "idx_visit_doctor", columnList = "doctor_id"),
    @Index(name = "idx_visit_status", columnList = "visit_type, status")
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Visit extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_type", nullable = false, length = 20)
    private VisitType visitType;

    @Column(name = "department_code", length = 50)
    private String departmentCode;

    @Column(name = "doctor_id")
    private String doctorId;

    @Column(name = "doctor_name", length = 200)
    private String doctorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private VisitStatus status = VisitStatus.REGISTERED;

    @Column(name = "token_number")
    private Integer tokenNumber;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public enum VisitType { OPD, IPD, EMERGENCY, DAY_CARE, HEALTH_CHECKUP }
    public enum VisitStatus { REGISTERED, WAITING, IN_PROGRESS, COMPLETED, CANCELLED }
}
