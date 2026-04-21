package com.siance.hm.patient.controller;

import com.siance.hm.common.dto.ApiResponse;
import com.siance.hm.common.dto.PagedResponse;
import com.siance.hm.patient.dto.*;
import com.siance.hm.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management", description = "Patient registration, search, and management APIs")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @Operation(summary = "Register new patient")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'REGISTRATION_CLERK')")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> registerPatient(
            @Valid @RequestBody PatientCreateDTO dto) {
        PatientResponseDTO patient = patientService.registerPatient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(patient));
    }

    @GetMapping("/{uhid}")
    @Operation(summary = "Get patient by UHID")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST', 'REGISTRATION_CLERK', 'BILLING')")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> getPatient(@PathVariable String uhid) {
        return ResponseEntity.ok(ApiResponse.ok(patientService.getPatientByUhid(uhid)));
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> getPatientById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(patientService.getPatientById(id)));
    }

    @PutMapping("/{uhid}")
    @Operation(summary = "Update patient information")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'REGISTRATION_CLERK')")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> updatePatient(
            @PathVariable String uhid, @Valid @RequestBody PatientUpdateDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(patientService.updatePatient(uhid, dto)));
    }

    @DeleteMapping("/{uhid}")
    @Operation(summary = "Soft-delete patient")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable String uhid) {
        patientService.softDeletePatient(uhid);
        return ResponseEntity.ok(ApiResponse.ok(null, "Patient deactivated successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search patients by criteria")
    public ResponseEntity<ApiResponse<PagedResponse<PatientResponseDTO>>> searchPatients(
            @ModelAttribute PatientSearchCriteria criteria,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(patientService.searchPatients(criteria, pageable)));
    }

    @PostMapping("/{uhid}/visits")
    @Operation(summary = "Create new visit for patient")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'REGISTRATION_CLERK', 'DOCTOR')")
    public ResponseEntity<ApiResponse<VisitResponseDTO>> createVisit(
            @PathVariable String uhid, @Valid @RequestBody VisitCreateDTO dto) {
        VisitResponseDTO visit = patientService.createVisit(uhid, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(visit));
    }

    @GetMapping("/{uhid}/visits")
    @Operation(summary = "Get patient visit history")
    public ResponseEntity<ApiResponse<PagedResponse<VisitResponseDTO>>> getVisitHistory(
            @PathVariable String uhid, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(patientService.getVisitHistory(uhid, pageable)));
    }
}
