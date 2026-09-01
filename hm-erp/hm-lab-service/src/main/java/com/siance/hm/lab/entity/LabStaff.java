package com.siance.hm.lab.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Which lab(s) a hospital staff member (hm-hospital-service's HospitalUser)
 * is assigned to. hm-lab-service checks this table directly for
 * authorization rather than trusting a JWT claim - see the note in
 * hm-hospital-service's HospitalUserService about why labIds enrichment
 * was left out of the login token.
 */
@Entity
@Table(name = "lab_staff", uniqueConstraints = @UniqueConstraint(columnNames = {"lab_id", "hospital_user_id"}))
@Getter
@Setter
@NoArgsConstructor
public class LabStaff extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @Column(name = "hospital_user_id", nullable = false)
    private UUID hospitalUserId;

    @Column(name = "hospital_id", nullable = false)
    private UUID hospitalId;

    @Column(length = 50)
    private String role;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
