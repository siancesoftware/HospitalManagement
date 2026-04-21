package com.siance.hm.patient.dto;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatientSearchCriteria {
    private String uhid;
    private String name;
    private String phone;
    private String nationalId;
    private String status;
}
