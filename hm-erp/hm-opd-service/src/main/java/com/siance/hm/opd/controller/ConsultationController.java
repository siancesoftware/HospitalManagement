package com.siance.hm.opd.controller;

import com.siance.hm.common.response.ApiResponse;
import com.siance.hm.opd.dto.ConsultationRequest;
import com.siance.hm.opd.dto.ConsultationResponse;
import com.siance.hm.opd.service.ConsultationService;
import com.siance.hm.security.principal.AuthPrincipal;
import com.siance.hm.security.web.CurrentUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Consultations")
@RestController
@RequestMapping("/consultations")
@RequiredArgsConstructor
@PreAuthorize("@hm.isHospitalContext()")
public class ConsultationController {

    private final ConsultationService consultationService;

    @PutMapping
    public ApiResponse<ConsultationResponse> createOrUpdate(@Valid @RequestBody ConsultationRequest request,
                                                              @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(consultationService.createOrUpdate(request, actor.id(), actor.hospitalId()));
    }

    @GetMapping("/visit/{visitId}")
    public ApiResponse<ConsultationResponse> getByVisit(@PathVariable UUID visitId, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(consultationService.getByVisit(visitId, actor.hospitalId()));
    }
}
