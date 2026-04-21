package com.siance.hm.patient.dto;

import lombok.*; import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ContactDTO {
    private UUID id;
    private String contactType;
    private String contactValue;
    private String contactName;
    private String relationship;
    private Boolean isPrimary;
    private Boolean isEmergency;
}
