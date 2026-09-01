package com.siance.hm.auth.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record UserProfileDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        List<String> roles,
        String status,
        boolean mfaEnabled,
        OffsetDateTime lastLoginAt
) {
}
