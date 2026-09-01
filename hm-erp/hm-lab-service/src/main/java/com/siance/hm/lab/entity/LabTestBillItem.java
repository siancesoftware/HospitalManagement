package com.siance.hm.lab.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "lab_test_bill_items")
@Getter
@Setter
@NoArgsConstructor
public class LabTestBillItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bill_id", nullable = false)
    private LabTestBill bill;

    @Column(name = "test_execution_id")
    private UUID testExecutionId;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
}
