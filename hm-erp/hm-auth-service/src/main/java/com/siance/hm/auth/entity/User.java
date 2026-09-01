package com.siance.hm.auth.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Platform identity record. Port of the {@code User} Prisma model.
 *
 * <p>Owns credentials only. Hospital/HospitalUser/Patient - and therefore
 * "which UserType this account represents" - now live in their owning
 * services (hm-hospital-service, hm-patient-service) and reference this
 * record only by {@code userId}, exactly like the original's optional
 * {@code hospital}/{@code hospitalStaff}/{@code patient} reverse relations,
 * just resolved via a service call instead of a Prisma include.
 */
@Entity
@Table(name = "users", indexes = @Index(name = "idx_users_email", columnList = "email"))
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "password_changed_at")
    private OffsetDateTime passwordChangedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "mfa_enabled", nullable = false)
    private boolean mfaEnabled = false;

    @Column(name = "failed_attempts", nullable = false)
    private int failedAttempts = 0;

    @Column(name = "locked_until")
    private OffsetDateTime lockedUntil;

    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    /** BCrypt hash of the current refresh token, never the raw value - rotated on every refresh. */
    @Column(name = "refresh_token_hash")
    private String refreshTokenHash;

    public String fullName() {
        return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
    }

    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(OffsetDateTime.now());
    }
}
