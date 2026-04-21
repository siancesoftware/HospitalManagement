package com.siance.hm.patient.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class VisitCreateDTO {
    @NotNull(message = "Visit type is required") private String visitType;
    private String departmentCode;
    private String doctorId;
    private String doctorName;
    private String notes;
}
