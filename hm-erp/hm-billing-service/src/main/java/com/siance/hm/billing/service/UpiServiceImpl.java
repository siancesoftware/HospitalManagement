package com.siance.hm.billing.service;

import com.siance.hm.billing.config.UpiProperties;
import com.siance.hm.billing.dto.ConfirmUpiPaymentRequest;
import com.siance.hm.billing.dto.CreateUpiPaymentRequest;
import com.siance.hm.billing.dto.PaymentResponse;
import com.siance.hm.billing.dto.UpiPaymentResponse;
import com.siance.hm.billing.entity.Payment;
import com.siance.hm.billing.entity.PaymentMode;
import com.siance.hm.billing.entity.PaymentStatus;
import com.siance.hm.billing.repository.PaymentRepository;
import com.siance.hm.common.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Port of the original UPI payment flow: build a standard {@code upi://pay}
 * deep link (works with any UPI app) plus a QR code image of it, rather
 * than integrating a specific payment gateway SDK. No external credentials
 * needed - only the QR *rendering* call goes out to a public image service.
 */
@Service
@RequiredArgsConstructor
public class UpiServiceImpl implements UpiService {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final UpiProperties upiProperties;

    @Override
    @Transactional
    public UpiPaymentResponse createIntent(
            CreateUpiPaymentRequest request,
            UUID collectedBy,
            UUID hospitalId
    ) {
        Payment payment = new Payment();
        payment.setHospitalId(hospitalId);
        payment.setPatientId(request.getPatientId());
        payment.setVisitId(request.getVisitId());
        payment.setAdmissionId(request.getAdmissionId());
        payment.setPurpose(request.getPurpose());
        payment.setAmount(request.getAmount());
        payment.setPaymentMode(PaymentMode.UPI);
        payment.setStatus(PaymentStatus.INITIATED);
        payment.setCollectedBy(collectedBy);
        payment.setUpiTxnRef("HM" + System.currentTimeMillis());

        payment = paymentRepository.save(payment);

        String payeeVpa = request.getPayeeVpa() != null
                ? request.getPayeeVpa()
                : upiProperties.getDefaultPayeeVpa();

        String payeeName = request.getPayeeName() != null
                ? request.getPayeeName()
                : upiProperties.getDefaultPayeeName();

        String note = request.getNote() != null
                ? request.getNote()
                : request.getPurpose().name();

        String deepLink = buildUpiDeepLink(
                payeeVpa,
                payeeName,
                request.getAmount(),
                note,
                payment.getUpiTxnRef()
        );

        String qrUrl = buildQrImageUrl(deepLink);

        return new UpiPaymentResponse(
                paymentService.toResponse(payment),
                deepLink,
                qrUrl
        );
    }

    /**
     * In production this is a webhook from your UPI PSP; exposed here as a
     * plain endpoint the front desk (or a polling client) can call once
     * they've confirmed payment in the UPI app - see the root README for
     * how to wire a real PSP webhook instead.
     */
    @Override
    @Transactional
    public PaymentResponse confirm(
            UUID paymentId,
            ConfirmUpiPaymentRequest request,
            UUID hospitalId
    ) {
        Payment payment = paymentService.find(
                paymentId,
                hospitalId
        );

        if (payment.getUpiTxnRef() == null
                || !payment.getUpiTxnRef()
                .equals(request.getUpiTxnRef())) {

            throw new ConflictException(
                    "Transaction reference does not match this payment."
            );
        }

        payment.setStatus(
                request.isSuccess()
                        ? PaymentStatus.SUCCESS
                        : PaymentStatus.FAILED
        );

        if (request.isSuccess()) {
            payment.setCollectedAt(OffsetDateTime.now());
        }

        return paymentService.toResponse(
                paymentRepository.save(payment)
        );
    }

    private String buildUpiDeepLink(
            String payeeVpa,
            String payeeName,
            BigDecimal amount,
            String note,
            String txnRef
    ) {
        return "upi://pay?pa=" + encode(payeeVpa)
                + "&pn=" + encode(payeeName)
                + "&am=" + amount.toPlainString()
                + "&cu=INR"
                + "&tn=" + encode(note)
                + "&tr=" + encode(txnRef);
    }

    private String buildQrImageUrl(String data) {
        return upiProperties.getQrImageBaseUrl()
                + "?size=300x300&data="
                + encode(data);
    }

    private String encode(String value) {
        return URLEncoder.encode(
                value,
                StandardCharsets.UTF_8
        );
    }
}