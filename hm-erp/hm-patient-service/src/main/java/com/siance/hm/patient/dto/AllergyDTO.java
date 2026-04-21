package com.siance.hm.patient.dto;

import lombok.*; import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AllergyDTO {
    private UUID id;
    private String allergen;
    private String allergyType;
    private String severity;
    private String reaction;
    private String verifiedBy;
}
