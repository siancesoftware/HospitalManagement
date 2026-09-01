package com.siance.hm.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/** Internal request: "does this email/password pair identify a valid, active user?" */
@Getter
@Setter
public class VerifyCredentialsRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
