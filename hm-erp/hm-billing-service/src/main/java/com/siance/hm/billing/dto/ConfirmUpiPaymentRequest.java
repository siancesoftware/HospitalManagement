package com.siance.hm.billing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/** Called once the UPI app confirms success (webhook, or manual front-desk confirmation for this simplified cut). */
@Getter
@Setter
public class ConfirmUpiPaymentRequest {
    @NotBlank
    private String upiTxnRef;
    private boolean success = true;
}
