package com.siance.hm.patient.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient_addresses")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PatientAddress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "address_type", length = 20)
    private String addressType; // CURRENT, PERMANENT

    @Column(name = "line1", nullable = false)
    private String line1;

    @Column(name = "line2")
    private String line2;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    @Builder.Default
    private String country = "India";

    @Column(name = "pin_code", length = 10)
    private String pinCode;
}
