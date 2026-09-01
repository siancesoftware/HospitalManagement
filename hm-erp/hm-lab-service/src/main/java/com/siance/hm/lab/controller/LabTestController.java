package com.siance.hm.lab.controller;

import com.siance.hm.common.response.ApiResponse;
import com.siance.hm.lab.dto.LabTestRequest;
import com.siance.hm.lab.dto.LabTestResponse;
import com.siance.hm.lab.service.LabTestService;
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

@Tag(name = "Lab Test Catalog")
@RestController
@RequestMapping("/lab-tests")
@RequiredArgsConstructor
@PreAuthorize("@hm.isHospitalContext()")
public class LabTestController {

    private final LabTestService labTestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LabTestResponse> create(@Valid @RequestBody LabTestRequest request, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(labTestService.create(request, actor.hospitalId()));
    }

    @GetMapping
    public ApiResponse<List<LabTestResponse>> search(@RequestParam(required = false) String q,
                                                        @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(labTestService.search(q, actor.hospitalId()));
    }

    @PatchMapping("/{id}")
    public ApiResponse<LabTestResponse> update(@PathVariable UUID id, @Valid @RequestBody LabTestRequest request,
                                                 @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(labTestService.update(id, request, actor.hospitalId()));
    }
}
