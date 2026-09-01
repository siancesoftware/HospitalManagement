package com.siance.hm.security.web;

import com.siance.hm.security.principal.AuthContext;

/**
 * Exposed as the {@code hm} bean so controllers can declaratively restrict
 * endpoints by principal type, mirroring the original's route-level guards
 * ({@code PlatformJwtGuard} / {@code HospitalJwtGuard}):
 *
 * <pre>{@code
 * @PreAuthorize("@hm.isPlatformAdmin()")      // was: @UseGuards(PlatformJwtGuard)
 * @PreAuthorize("@hm.isHospitalContext()")    // was: @UseGuards(HospitalJwtGuard)
 * }</pre>
 */
public class SecurityExpressions {

    public boolean isPlatformAdmin() {
        return AuthContext.current().map(p -> p.isPlatformAdmin()).orElse(false);
    }

    public boolean isHospitalContext() {
        return AuthContext.current().map(p -> p.isHospitalContext()).orElse(false);
    }

    public boolean isHospitalOwner() {
        return AuthContext.current()
                .map(p -> p.type() == com.siance.hm.security.principal.UserType.HOSPITAL)
                .orElse(false);
    }

    public boolean isPatient() {
        return AuthContext.current().map(p -> p.isPatient()).orElse(false);
    }

    /** True if the current principal belongs to hospitalId (hospital owner/staff/patient scoped to it). */
    public boolean ownsHospital(java.util.UUID hospitalId) {
        return AuthContext.current().map(p -> hospitalId != null && hospitalId.equals(p.hospitalId())).orElse(false);
    }
}
