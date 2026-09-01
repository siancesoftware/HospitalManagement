package com.siance.hm.opd.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ConsultationRequest {
    @NotNull
    private UUID visitId;
    @NotNull
    private UUID patientId;
    private String subjective;
    private String objective;
    private String assessment;
    private String plan;
    private String diagnosis;
}
