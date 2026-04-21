package com.siance.hm.patient.dto;

import lombok.*; import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AddressDTO {
    private UUID id;
    private String addressType;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String country;
    private String pinCode;
}
