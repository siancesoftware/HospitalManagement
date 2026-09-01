package com.siance.hm.auth.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Port of the {@code UserRole} join model - who has which role, optionally scoped and time-limited. */
@Entity
@Table(name = "user_roles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id", "scope_type", "scope_id"}))
@Getter
@Setter
@NoArgsConstructor
public class UserRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "scope_type", length = 30)
    private String scopeType;

    @Column(name = "scope_id", length = 50)
    private String scopeId;

    @Column(name = "granted_by")
    private UUID grantedBy;

    @Column(name = "granted_at", nullable = false)
    private OffsetDateTime grantedAt = OffsetDateTime.now();

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}
