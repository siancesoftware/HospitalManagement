package com.siance.hm.billing.entity;

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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Port of {@code Payment} - a single collection against a patient, whatever
 * its source (OPD visit, IPD admission, lab or pharmacy bill). Those source
 * references are plain UUID columns since each lives in a different
 * service's database.
 */
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payments_hospital_id", columnList = "hospital_id"),
        @Index(name = "idx_payments_patient_id", columnList = "patient_id")
})
@Getter
@Setter
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Column(name = "hospital_id", nullable = false)
    private UUID hospitalId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "visit_id")
    private UUID visitId;

    @Column(name = "admission_id")
    private UUID admissionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentPurpose purpose = PaymentPurpose.OTHER;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false, length = 20)
    private PaymentMode paymentMode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.INITIATED;

    @Column(name = "reference_number", length = 100)
    private String referenceNumber;

    /** UPI deep-link transaction reference (tr= param) - see UpiService. */
    @Column(name = "upi_txn_ref", length = 100)
    private String upiTxnRef;

    @Column(name = "collected_by")
    private UUID collectedBy;

    @Column(name = "collected_at")
    private OffsetDateTime collectedAt;
}
