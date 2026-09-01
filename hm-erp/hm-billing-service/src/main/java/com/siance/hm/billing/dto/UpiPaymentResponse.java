package com.siance.hm.billing.dto;

public record UpiPaymentResponse(PaymentResponse payment, String upiDeepLink, String qrCodeImageUrl) {
}
