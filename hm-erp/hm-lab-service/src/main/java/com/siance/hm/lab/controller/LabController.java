package com.siance.hm.lab.controller;

import com.siance.hm.common.response.ApiResponse;
import com.siance.hm.lab.dto.LabRequest;
import com.siance.hm.lab.dto.LabResponse;
import com.siance.hm.lab.service.LabService;
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

@Tag(name = "Labs")
@RestController
@RequestMapping("/labs")
@RequiredArgsConstructor
@PreAuthorize("@hm.isHospitalContext()")
public class LabController {

    private final LabService labService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LabResponse> create(@Valid @RequestBody LabRequest request, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(labService.create(request, actor.hospitalId()));
    }

    @GetMapping
    public ApiResponse<List<LabResponse>> list(@CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(labService.list(actor.hospitalId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<LabResponse> getById(@PathVariable UUID id, @CurrentUser AuthPrincipal actor) {
        return ApiResponse.of(labService.getById(id, actor.hospitalId()));
    }
}
