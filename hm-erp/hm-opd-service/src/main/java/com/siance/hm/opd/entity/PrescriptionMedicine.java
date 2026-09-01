package com.siance.hm.opd.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Line item - port of {@code PrescriptionMedicine}. drugCode references hm-pharmacy-service's Medicine catalog. */
@Entity
@Table(name = "prescription_medicines")
@Getter
@Setter
@NoArgsConstructor
public class PrescriptionMedicine extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @Column(name = "drug_code", length = 50)
    private String drugCode;

    @Column(name = "drug_name", nullable = false, length = 200)
    private String drugName;

    @Column(length = 100)
    private String dose;

    @Column(length = 100)
    private String frequency;

    @Column(length = 50)
    private String route;

    @Column(name = "duration_days")
    private Integer durationDays;

    private Integer quantity;

    @Column(columnDefinition = "text")
    private String instructions;

    @Column(name = "substitution_allowed", nullable = false)
    private boolean substitutionAllowed = true;
}
