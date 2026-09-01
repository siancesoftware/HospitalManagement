package com.siance.hm.lab.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderLabTestRequest {
    @NotNull
    private UUID labId;
    @NotNull
    private UUID testId;
    @NotNull
    private UUID patientId;
    private UUID visitId;
    private String prescriptionLabTestRef;
}
