package com.siance.hm.billing.controller;

import com.siance.hm.billing.dto.CreatePaymentRequest;
import com.siance.hm.billing.dto.PaymentResponse;
import com.siance.hm.billing.service.PaymentService;
import com.siance.hm.common.response.ApiResponse;
import com.siance.hm.common.response.PagedResult;
import com.siance.hm.common.web.PageQuery;
import com.siance.hm.security.principal.AuthPrincipal;
import com.siance.hm.security.web.CurrentUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/** Port of the original payment module's cash/card/insurance collection endpoints. */
@Tag(name = "Payments")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@PreAuthorize("@hm.isHospitalContext()")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PaymentResponse> collect(@Valid @RequestBody CreatePaymentRequest request, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(paymentService.collect(request, actor.id(), actor.hospitalId()));
    }

    @GetMapping
    public PagedResult<PaymentResponse> list(PageQuery query, @CurrentUser AuthPrincipal actor) {
        return paymentService.list(query, actor.hospitalId());
    }

    @GetMapping("/{id}")
    public ApiResponse<PaymentResponse> getById(@PathVariable UUID id, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(paymentService.getById(id, actor.hospitalId()));
    }

    @GetMapping("/patient/{patientId}")
    public ApiResponse<List<PaymentResponse>> forPatient(@PathVariable UUID patientId, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(paymentService.forPatient(patientId, actor.hospitalId()));
    }
}
