package com.siance.hm.lab.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lab_test_bills")
@Getter
@Setter
@NoArgsConstructor
public class LabTestBill extends BaseEntity {

    @Column(name = "hospital_id", nullable = false)
    private UUID hospitalId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "visit_id")
    private UUID visitId;

    @Column(name = "bill_number", nullable = false, unique = true, length = 30)
    private String billNumber;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "paid_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BillStatus status = BillStatus.UNPAID;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LabTestBillItem> items = new ArrayList<>();
}
