package com.siance.hm.opd.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/** Port of {@code Consultation} - SOAP-format clinical notes for a Visit. */
@Entity
@Table(name = "consultations")
@Getter
@Setter
@NoArgsConstructor
public class Consultation extends BaseEntity {

    @Column(name = "visit_id", nullable = false)
    private UUID visitId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @Column(name = "hospital_id", nullable = false)
    private UUID hospitalId;

    @Column(columnDefinition = "text")
    private String subjective;

    @Column(columnDefinition = "text")
    private String objective;

    @Column(columnDefinition = "text")
    private String assessment;

    @Column(columnDefinition = "text")
    private String plan;

    /** Free-text or ICD-10 code list, semicolon separated for this simplified cut. */
    @Column(columnDefinition = "text")
    private String diagnosis;
}
