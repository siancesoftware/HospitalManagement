package com.siance.hm.lab.controller;

import com.siance.hm.common.response.ApiResponse;
import com.siance.hm.common.response.PagedResult;
import com.siance.hm.common.web.PageQuery;
import com.siance.hm.lab.dto.CollectSampleRequest;
import com.siance.hm.lab.dto.EnterResultRequest;
import com.siance.hm.lab.dto.LabTestExecutionResponse;
import com.siance.hm.lab.dto.OrderLabTestRequest;
import com.siance.hm.lab.service.LabTestExecutionService;
import com.siance.hm.security.principal.AuthPrincipal;
import com.siance.hm.security.web.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/** Port of the original lab order/sample/result/validate endpoints. */
@Tag(name = "Lab Orders")
@RestController
@RequestMapping("/lab-orders")
@RequiredArgsConstructor
@PreAuthorize("@hm.isHospitalContext()")
public class LabTestExecutionController {

    private final LabTestExecutionService executionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LabTestExecutionResponse> order(@Valid @RequestBody OrderLabTestRequest request,
                                                          @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(executionService.order(request, actor.id(), actor.hospitalId()));
    }

    @GetMapping
    public PagedResult<LabTestExecutionResponse> list(PageQuery query, @CurrentUser AuthPrincipal actor) {
        return executionService.list(query, actor.hospitalId());
    }

    @GetMapping("/{id}")
    public ApiResponse<LabTestExecutionResponse> getById(@PathVariable UUID id, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(executionService.getById(id, actor.hospitalId()));
    }

    @GetMapping("/patient/{patientId}")
    public ApiResponse<List<LabTestExecutionResponse>> forPatient(@PathVariable UUID patientId, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(executionService.forPatient(patientId, actor.hospitalId()));
    }

    @PatchMapping("/{id}/collect-sample")
    public ApiResponse<LabTestExecutionResponse> collectSample(@PathVariable UUID id, @Valid @RequestBody CollectSampleRequest request,
                                                                  @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(executionService.collectSample(id, request.getSampleBarcode(), actor.hospitalId()));
    }

    @PatchMapping("/{id}/result")
    public ApiResponse<LabTestExecutionResponse> enterResult(@PathVariable UUID id, @Valid @RequestBody EnterResultRequest request,
                                                                @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(executionService.enterResult(id, request.getResultValuesJson(), actor.hospitalId()));
    }

    @PatchMapping("/{id}/validate")
    @Operation(summary = "Two-step validation: only a distinct validator should call this after result entry")
    public ApiResponse<LabTestExecutionResponse> validate(@PathVariable UUID id, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(executionService.validate(id, actor.id(), actor.hospitalId()));
    }
}
