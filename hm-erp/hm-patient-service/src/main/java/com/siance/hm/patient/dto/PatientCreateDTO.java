package com.siance.hm.patient.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatientCreateDTO {
    @NotBlank(message = "First name is required")
    @Size(max = 100) private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100) private String lastName;

    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
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
    private String referralSource;
    private Map<String, Object> metadata;
    private List<ContactDTO> contacts;
    private List<AddressDTO> addresses;
    private List<AllergyDTO> allergies;
}
