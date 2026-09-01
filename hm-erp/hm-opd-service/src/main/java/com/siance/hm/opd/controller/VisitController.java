package com.siance.hm.opd.controller;

import com.siance.hm.common.response.ApiResponse;
import com.siance.hm.common.response.PagedResult;
import com.siance.hm.common.web.PageQuery;
import com.siance.hm.opd.dto.CreateVisitRequest;
import com.siance.hm.opd.dto.UpdateVisitStatusRequest;
import com.siance.hm.opd.dto.VisitResponse;
import com.siance.hm.opd.service.VisitService;
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

/** Port of the original hm-opd-service visit endpoints. */
@Tag(name = "Visits")
@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
@PreAuthorize("@hm.isHospitalContext()")
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VisitResponse> create(@Valid @RequestBody CreateVisitRequest request, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(visitService.create(request, actor.hospitalId()));
    }

    @GetMapping
    public PagedResult<VisitResponse> list(PageQuery query, @CurrentUser AuthPrincipal actor) {
        return visitService.list(query, actor.hospitalId());
    }

    @GetMapping("/{id}")
    public ApiResponse<VisitResponse> getById(@PathVariable UUID id, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(visitService.getById(id, actor.hospitalId()));
    }

    @GetMapping("/patient/{patientId}")
    public ApiResponse<List<VisitResponse>> forPatient(@PathVariable UUID patientId, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(visitService.forPatient(patientId, actor.hospitalId()));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<VisitResponse> updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateVisitStatusRequest request,
                                                     @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(visitService.updateStatus(id, request.getStatus(), actor.hospitalId()));
    }
}
