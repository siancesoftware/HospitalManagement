package com.siance.hm.billing.dto;

import com.siance.hm.billing.entity.PaymentPurpose;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreateUpiPaymentRequest {
    @NotNull
    private UUID patientId;
    private UUID visitId;
    private UUID admissionId;
    @NotNull
    private PaymentPurpose purpose;
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
    private String note;
    /** Optional per-hospital override; falls back to hm.billing.upi.default-* config. */
    private String payeeVpa;
    private String payeeName;
}
