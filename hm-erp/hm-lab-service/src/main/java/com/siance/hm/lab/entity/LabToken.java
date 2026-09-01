package com.siance.hm.lab.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "lab_tokens")
@Getter
@Setter
@NoArgsConstructor
public class LabToken extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @Column(name = "hospital_id", nullable = false)
    private UUID hospitalId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "token_number", nullable = false)
    private int tokenNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LabTokenStatus status = LabTokenStatus.WAITING;
}
