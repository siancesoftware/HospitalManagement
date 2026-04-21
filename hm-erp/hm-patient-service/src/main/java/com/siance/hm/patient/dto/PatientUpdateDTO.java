package com.siance.hm.patient.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatientUpdateDTO {
    @Size(max = 100) private String firstName;
    @Size(max = 100) private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String maritalStatus;
    private String nationality;
    private String nationalId;
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String primaryPhone;
    @Email(message = "Invalid email")
    private String email;
    private String photoUrl;
    private Map<String, Object> metadata;
}
