package com.siance.hm.auth.dto;

import java.util.List;
import java.util.UUID;

/** Minimal identity snapshot returned to other services - never includes the password hash. */
public record UserSummary(
        UUID id,
        String firstName,
        String lastName,
        String email,
        boolean active,
        List<String> roles
) {
}
