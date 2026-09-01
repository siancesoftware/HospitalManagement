package com.siance.hm.opd.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/** Port of {@code AdmissionAdvice} - a doctor's order/instruction during a ward round. */
@Entity
@Table(name = "admission_advices")
@Getter
@Setter
@NoArgsConstructor
public class AdmissionAdvice extends BaseEntity {

    @Column(name = "admission_id", nullable = false)
    private UUID admissionId;

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @Column(nullable = false, columnDefinition = "text")
    private String advice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdmissionAdviceStatus status = AdmissionAdviceStatus.PENDING;
}
