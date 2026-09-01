package com.siance.hm.security.principal;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * The authenticated caller, equivalent to the {@code request.user} object the
 * original JwtStrategy.validate() built per-request.
 *
 * <p><b>Design note:</b> the original re-queried Postgres on every request to
 * assemble this object (fresh role/lab-assignment/active-status data). In a
 * database-per-service microservice split that lookup would mean a
 * synchronous call from every service to hm-hospital-service on every single
 * authenticated request. Instead, this principal is built entirely from
 * signed JWT claims (stateless verification) - the standard microservices
 * JWT pattern. The trade-off: role/assignment changes take effect on next
 * login/refresh rather than immediately. See the root README
 * ("Auth design decisions") for how to add a revocation list if you need
 * immediate effect.
 */
public record AuthPrincipal(
        UUID id,
        String email,
        UserType type,
        UUID hospitalId,
        UUID hospitalUserId,
        String staffType,
        UUID staffTypeId,
        List<String> roles,
        List<UUID> labIds
) implements Serializable {

    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }

    public boolean isPlatformAdmin() {
        return type == UserType.SUPER_ADMIN;
    }

    public boolean isHospitalContext() {
        return type == UserType.HOSPITAL || type == UserType.HOSPITAL_STAFF;
    }

    public boolean isPatient() {
        return type == UserType.PATIENT;
    }

    public UUID primaryLabId() {
        return labIds == null || labIds.isEmpty() ? null : labIds.get(0);
    }
}
