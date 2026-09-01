package com.siance.hm.lab.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Port of {@code LabTestExecution} - order-to-result lifecycle for a single test on a single patient. */
@Entity
@Table(name = "lab_test_executions", indexes = {
        @Index(name = "idx_lte_hospital_id", columnList = "hospital_id"),
        @Index(name = "idx_lte_patient_id", columnList = "patient_id")
})
@Getter
@Setter
@NoArgsConstructor
public class LabTestExecution extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_id", nullable = false)
    private LabTest test;

    @Column(name = "hospital_id", nullable = false)
    private UUID hospitalId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "visit_id")
    private UUID visitId;

    /** hm-opd-service's PrescriptionLabTest.id, if this order originated from a prescription. */
    @Column(name = "prescription_lab_test_ref")
    private String prescriptionLabTestRef;

    @Column(name = "ordered_by")
    private UUID orderedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LabTestExecutionStatus status = LabTestExecutionStatus.ORDERED;

    @Column(name = "sample_barcode", length = 50)
    private String sampleBarcode;

    @Column(name = "sample_collected_at")
    private OffsetDateTime sampleCollectedAt;

    /** Parameter name -> value, as JSON - simplified vs. the original's normalized test_parameters/lab_results tables. */
    @Column(name = "result_values", columnDefinition = "jsonb")
    private String resultValuesJson;

    @Column(name = "result_entered_at")
    private OffsetDateTime resultEnteredAt;

    @Column(name = "validated_by")
    private UUID validatedBy;

    @Column(name = "validated_at")
    private OffsetDateTime validatedAt;

    @Column(name = "report_url")
    private String reportUrl;
}
