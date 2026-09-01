package com.siance.hm.lab.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/** A physical lab/section within a hospital (e.g. "Main Pathology Lab", "Radiology Lab"). */
@Entity
@Table(name = "labs")
@Getter
@Setter
@NoArgsConstructor
public class Lab extends BaseEntity {

    @Column(name = "hospital_id", nullable = false)
    private UUID hospitalId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 100)
    private String location;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
