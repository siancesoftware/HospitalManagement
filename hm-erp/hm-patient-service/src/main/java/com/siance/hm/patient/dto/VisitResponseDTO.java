package com.siance.hm.patient.dto;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class VisitResponseDTO {
    private UUID id;
    private String patientUhid;
    private String visitType;
    private String departmentCode;
    private String doctorId;
    private String doctorName;
    private String status;
    private Integer tokenNumber;
    private Instant startedAt;
    private Instant endedAt;
    private String notes;
    private Instant createdAt;
}
