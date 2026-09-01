package com.siance.hm.billing.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id, UUID patientId, UUID visitId, UUID admissionId, String purpose, BigDecimal amount,
        String paymentMode, String status, String referenceNumber, String upiTxnRef, OffsetDateTime collectedAt
) {
}
