package com.siance.hm.lab.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "lab_test_payments")
@Getter
@Setter
@NoArgsConstructor
public class LabTestPayment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bill_id", nullable = false)
    private LabTestBill bill;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false, length = 20)
    private PaymentMode paymentMode;

    @Column(name = "reference_number", length = 100)
    private String referenceNumber;

    @Column(name = "paid_at", nullable = false)
    private OffsetDateTime paidAt = OffsetDateTime.now();
}
