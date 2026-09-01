package com.siance.hm.opd.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Line item - port of {@code PrescriptionLabTest}. testCode references hm-lab-service's test catalog. */
@Entity
@Table(name = "prescription_lab_tests")
@Getter
@Setter
@NoArgsConstructor
public class PrescriptionLabTest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @Column(name = "test_code", length = 50)
    private String testCode;

    @Column(name = "test_name", nullable = false, length = 200)
    private String testName;

    @Column(length = 20)
    private String urgency = "ROUTINE";

    @Column(name = "clinical_info", columnDefinition = "text")
    private String clinicalInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.ORDERED;

    /** Set once hm-lab-service has processed the order - the lab_orders.id it created. */
    @Column(name = "lab_order_reference")
    private String labOrderReference;
}
