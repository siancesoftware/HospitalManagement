package com.siance.hm.patient.dto;

import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatientResponseDTO {
    private UUID id;
    private String uhid;
    private String firstName;
    private String lastName;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String maritalStatus;
    private String nationality;
    private String nationalId;
    private String primaryPhone;
    private String email;
    private String photoUrl;
    private String referralSource;
    private String status;
    private Map<String, Object> metadata;
    private List<ContactDTO> contacts;
    private List<AddressDTO> addresses;
    private List<AllergyDTO> allergies;
    private Instant createdAt;
    private Instant updatedAt;
}
