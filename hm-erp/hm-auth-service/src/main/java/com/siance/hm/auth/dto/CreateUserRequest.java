package com.siance.hm.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Internal, service-to-service request used by hm-hospital-service (and any
 * other service that owns a principal type) to create the backing
 * credential record when it registers a Hospital or HospitalUser - the
 * cross-service equivalent of the original's single-transaction
 * {@code prisma.user.create()} + related-record create.
 */
@Getter
@Setter
public class CreateUserRequest {

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(min = 8, max = 72)
    private String password;

    /** Role codes to attach immediately, e.g. ["HOSPITAL_ADMIN"] or ["DOCTOR"]. */
    private List<String> roleCodes;
}
