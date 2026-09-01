package com.siance.hm.billing.service;

import com.siance.hm.billing.dto.CreatePaymentRequest;
import com.siance.hm.billing.dto.PaymentResponse;
import com.siance.hm.billing.entity.Payment;
import com.siance.hm.common.response.PagedResult;
import com.siance.hm.common.web.PageQuery;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PaymentResponse collect(
            CreatePaymentRequest request,
            UUID collectedBy,
            UUID hospitalId
    );

    PaymentResponse getById(
            UUID id,
            UUID hospitalId
    );

    PagedResult<PaymentResponse> list(
            PageQuery query,
            UUID hospitalId
    );

    List<PaymentResponse> forPatient(
            UUID patientId,
            UUID hospitalId
    );

    Payment find(
            UUID id,
            UUID hospitalId
    );

    PaymentResponse toResponse(Payment payment);
}