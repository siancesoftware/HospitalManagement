package com.siance.hm.patient.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "patients", indexes = {
    @Index(name = "idx_patient_uhid", columnList = "uhid", unique = true),
    @Index(name = "idx_patient_national_id", columnList = "national_id"),
    @Index(name = "idx_patient_phone", columnList = "primary_phone"),
    @Index(name = "idx_patient_name", columnList = "first_name, last_name")
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Patient extends BaseEntity {

    @Column(name = "uhid", unique = true, nullable = false, length = 20)
    private String uhid;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 20)
    private Gender gender;

    @Column(name = "blood_group", length = 10)
    private String bloodGroup;

    @Column(name = "marital_status", length = 20)
    private String maritalStatus;

    @Column(name = "nationality", length = 50)
    private String nationality;

    @Column(name = "national_id", length = 50)
    private String nationalId;

    @Column(name = "primary_phone", length = 20)
    private String primaryPhone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "referral_source", length = 50)
    private String referralSource;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private PatientStatus status = PatientStatus.ACTIVE;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PatientContact> contacts = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PatientAddress> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PatientAllergy> allergies = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Visit> visits = new ArrayList<>();

    public void addContact(PatientContact contact) {
        contacts.add(contact);
        contact.setPatient(this);
    }

    public void addAddress(PatientAddress address) {
        addresses.add(address);
        address.setPatient(this);
    }

    public void addAllergy(PatientAllergy allergy) {
        allergies.add(allergy);
        allergy.setPatient(this);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public enum Gender { MALE, FEMALE, OTHER, UNKNOWN }
    public enum PatientStatus { ACTIVE, INACTIVE, DECEASED, MERGED }
}
