package com.siance.hm.billing.controller;

import com.siance.hm.billing.dto.ConfirmUpiPaymentRequest;
import com.siance.hm.billing.dto.CreateUpiPaymentRequest;
import com.siance.hm.billing.dto.PaymentResponse;
import com.siance.hm.billing.dto.UpiPaymentResponse;
import com.siance.hm.billing.service.UpiService;
import com.siance.hm.common.response.ApiResponse;
import com.siance.hm.security.principal.AuthPrincipal;
import com.siance.hm.security.web.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "UPI Payments")
@RestController
@RequestMapping("/payments/upi")
@RequiredArgsConstructor
@PreAuthorize("@hm.isHospitalContext()")
public class UpiPaymentController {

    private final UpiService upiService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a UPI payment intent", description = "Returns a upi:// deep link and a QR code image URL to show the patient.")
    public ApiResponse<UpiPaymentResponse> createIntent(@Valid @RequestBody CreateUpiPaymentRequest request,
                                                          @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(upiService.createIntent(request, actor.id(), actor.hospitalId()));
    }

    @PostMapping("/{paymentId}/confirm")
    @Operation(summary = "Confirm a UPI payment", description = "Stand-in for a PSP webhook - see UpiService Javadoc.")
    public ApiResponse<PaymentResponse> confirm(@PathVariable UUID paymentId, @Valid @RequestBody ConfirmUpiPaymentRequest request,
                                                  @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(upiService.confirm(paymentId, request, actor.hospitalId()));
    }
}
