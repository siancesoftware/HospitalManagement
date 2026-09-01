package com.siance.hm.billing.service;

import com.siance.hm.billing.dto.CreatePaymentRequest;
import com.siance.hm.billing.dto.PaymentResponse;
import com.siance.hm.billing.entity.Payment;
import com.siance.hm.billing.entity.PaymentStatus;
import com.siance.hm.billing.repository.PaymentRepository;
import com.siance.hm.common.exception.ResourceNotFoundException;
import com.siance.hm.common.response.PagedResult;
import com.siance.hm.common.web.PageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Port of the original payment module's cash/card/insurance path
 * (immediate success, no external gateway hop).
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponse collect(
            CreatePaymentRequest request,
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
        payment.setPaymentMode(request.getPaymentMode());
        payment.setReferenceNumber(request.getReferenceNumber());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setCollectedBy(collectedBy);
        payment.setCollectedAt(OffsetDateTime.now());

        return toResponse(paymentRepository.save(payment));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getById(
            UUID id,
            UUID hospitalId
    ) {
        return toResponse(find(id, hospitalId));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResult<PaymentResponse> list(
            PageQuery query,
            UUID hospitalId
    ) {
        Page<Payment> page =
                paymentRepository.findByHospitalId(
                        hospitalId,
                        query.toPageable("createdAt")
                );

        List<PaymentResponse> data =
                page.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return PagedResult.fromZeroBasedPage(
                data,
                page.getTotalElements(),
                page.getNumber(),
                query.getLimit()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> forPatient(
            UUID patientId,
            UUID hospitalId
    ) {
        return paymentRepository
                .findByHospitalIdAndPatientId(
                        hospitalId,
                        patientId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Payment find(
            UUID id,
            UUID hospitalId
    ) {
        return paymentRepository
                .findByIdAndHospitalId(id, hospitalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + id
                        )
                );
    }

    @Override
    public PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(
                p.getId(),
                p.getPatientId(),
                p.getVisitId(),
                p.getAdmissionId(),
                p.getPurpose().name(),
                p.getAmount(),
                p.getPaymentMode().name(),
                p.getStatus().name(),
                p.getReferenceNumber(),
                p.getUpiTxnRef(),
                p.getCollectedAt()
        );
    }
}