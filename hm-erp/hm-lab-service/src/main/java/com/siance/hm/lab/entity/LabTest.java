package com.siance.hm.lab.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/** Test catalog entry - port of {@code LabTest} / {@code test_master}. */
@Entity
@Table(name = "lab_tests", uniqueConstraints = @UniqueConstraint(columnNames = {"hospital_id", "test_code"}))
@Getter
@Setter
@NoArgsConstructor
public class LabTest extends BaseEntity {

    @Column(name = "hospital_id", nullable = false)
    private UUID hospitalId;

    @Column(name = "test_code", nullable = false, length = 30)
    private String testCode;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "loinc_code", length = 30)
    private String loincCode;

    @Column(length = 100)
    private String section;

    @Column(name = "sample_type", length = 50)
    private String sampleType;

    @Column(name = "tat_hours")
    private Integer tatHours;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    /** Reference range / units / critical values as free-form JSON - one row per test here vs. the original's separate test_parameters table (simplified for this cut). */
    @Column(name = "reference_info", columnDefinition = "jsonb")
    private String referenceInfoJson;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
