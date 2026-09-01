package com.siance.hm.security.jwt;

import com.siance.hm.security.principal.UserType;

import java.util.List;
import java.util.UUID;

/**
 * Input to {@link JwtTokenProvider#generateAccessToken}, equivalent of the
 * original {@code JwtPayload} interface built before signing.
 */
public record TokenClaims(
        UUID subject,
        String email,
        UserType type,
        UUID hospitalId,
        UUID hospitalUserId,
        String staffType,
        UUID staffTypeId,
        List<String> roles,
        List<UUID> labIds
) {

    public static TokenClaims platformAdmin(UUID userId, String email, List<String> roles) {
        return new TokenClaims(userId, email, UserType.SUPER_ADMIN, null, null, null, null, roles, null);
    }

    public static TokenClaims hospital(UUID userId, UUID hospitalId, List<String> roles) {
        return new TokenClaims(userId, null, UserType.HOSPITAL, hospitalId, null, null, null, roles, null);
    }

    public static TokenClaims hospitalStaff(UUID userId, UUID hospitalId, UUID hospitalUserId, String staffType,
                                             UUID staffTypeId, List<String> roles, List<UUID> labIds) {
        return new TokenClaims(userId, null, UserType.HOSPITAL_STAFF, hospitalId, hospitalUserId, staffType,
                staffTypeId, roles, labIds);
    }

    public static TokenClaims patient(UUID patientId, UUID hospitalId) {
        return new TokenClaims(patientId, null, UserType.PATIENT, hospitalId, null, null, null, List.of(), null);
    }
}
