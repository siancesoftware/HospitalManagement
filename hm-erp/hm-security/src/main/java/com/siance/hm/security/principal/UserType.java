package com.siance.hm.security.principal;

/**
 * The four principal types the original JwtStrategy branched on
 * ('super_admin' | 'hospital' | 'hospital_staff' | 'patient').
 * Carried as the "type" claim on every issued token.
 */
public enum UserType {
    SUPER_ADMIN,
    HOSPITAL,
    HOSPITAL_STAFF,
    PATIENT
}
