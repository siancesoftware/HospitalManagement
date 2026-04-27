package com.siance.hm.patient.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "patient_allergies")
@Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
public class PatientAllergy extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "allergen", nullable = false, length = 200)
    private String allergen;

    @Column(name = "allergy_type", length = 30)
    private String allergyType; // DRUG, FOOD, ENVIRONMENTAL

    @Column(name = "severity", length = 20)
    private String severity; // MILD, MODERATE, SEVERE, LIFE_THREATENING

    @Column(name = "reaction", length = 500)
    private String reaction;

    @Column(name = "verified_by")
    private String verifiedBy;
}
