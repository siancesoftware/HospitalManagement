package com.siance.hm.billing.dto;

import com.siance.hm.billing.entity.PaymentMode;
import com.siance.hm.billing.entity.PaymentPurpose;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreatePaymentRequest {
    @NotNull
    private UUID patientId;
    private UUID visitId;
    private UUID admissionId;
    @NotNull
    private PaymentPurpose purpose;
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
    @NotNull
    private PaymentMode paymentMode;
    private String referenceNumber;
}
