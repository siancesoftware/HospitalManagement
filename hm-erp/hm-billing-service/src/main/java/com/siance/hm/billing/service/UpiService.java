package com.siance.hm.billing.service;

import com.siance.hm.billing.dto.ConfirmUpiPaymentRequest;
import com.siance.hm.billing.dto.CreateUpiPaymentRequest;
import com.siance.hm.billing.dto.PaymentResponse;
import com.siance.hm.billing.dto.UpiPaymentResponse;

import java.util.UUID;

public interface UpiService {

    UpiPaymentResponse createIntent(
            CreateUpiPaymentRequest request,
            UUID collectedBy,
            UUID hospitalId
    );

    PaymentResponse confirm(
            UUID paymentId,
            ConfirmUpiPaymentRequest request,
            UUID hospitalId
    );
}