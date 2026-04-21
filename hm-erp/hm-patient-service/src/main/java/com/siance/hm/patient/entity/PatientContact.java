package com.siance.hm.patient.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient_contacts")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PatientContact extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "contact_type", nullable = false, length = 30)
    private String contactType; // PHONE, EMAIL, EMERGENCY

    @Column(name = "contact_value", nullable = false, length = 150)
    private String contactValue;

    @Column(name = "contact_name", length = 100)
    private String contactName; // for emergency contacts

    @Column(name = "relationship", length = 50)
    private String relationship; // for emergency contacts

    @Column(name = "is_primary")
    @Builder.Default
    private Boolean isPrimary = false;

    @Column(name = "is_emergency")
    @Builder.Default
    private Boolean isEmergency = false;
}
