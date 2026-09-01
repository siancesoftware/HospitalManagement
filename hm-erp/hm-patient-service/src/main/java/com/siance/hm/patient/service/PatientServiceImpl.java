package com.siance.hm.patient.service;

import com.siance.hm.common.dto.PagedResponse;
import com.siance.hm.common.exception.DuplicateResourceException;
import com.siance.hm.common.util.UHIDGenerator;
import com.siance.hm.kafka.event.KafkaTopics;
import com.siance.hm.kafka.producer.EventPublisher;
import com.siance.hm.patient.dto.*;
import com.siance.hm.patient.entity.*;
import com.siance.hm.patient.event.PatientRegisteredEvent;
import com.siance.hm.patient.event.PatientVisitCreatedEvent;
import com.siance.hm.patient.exception.PatientNotFoundException;
import com.siance.hm.patient.mapper.PatientMapper;
import com.siance.hm.patient.repository.PatientRepository;
import com.siance.hm.patient.repository.VisitRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final PatientMapper patientMapper;
    private final EventPublisher eventPublisher;

    @Value("${app.uhid.prefix:HM}")
    private String uhidPrefix;

    // ============================================================
    // PATIENT MANAGEMENT
    // ============================================================

    @Override
    @Transactional
    public PatientResponseDTO registerPatient(PatientCreateDTO dto) {

        log.info(
                "Registering new patient: {} {}",
                dto.getFirstName(),
                dto.getLastName()
        );

        // Duplicate check
        if (dto.getNationalId() != null
                && patientRepository.existsByNationalIdAndDeletedAtIsNull(
                        dto.getNationalId())) {

            throw new DuplicateResourceException(
                    "Patient",
                    "nationalId",
                    dto.getNationalId()
            );
        }

        Patient patient = patientMapper.toEntity(dto);

        // Generate UHID
        Long seq = patientRepository.getNextUhidSequence();

        patient.setUhid(
                UHIDGenerator.generate(uhidPrefix, seq)
        );

        patient.setStatus(
                Patient.PatientStatus.ACTIVE
        );

        // ========================================================
        // CONTACTS
        // ========================================================

        if (dto.getContacts() != null) {

            dto.getContacts().forEach(contactDTO -> {

                PatientContact contact =
                        patientMapper.toContactEntity(contactDTO);

                patient.addContact(contact);
            });
        }

        // ========================================================
        // ADDRESSES
        // ========================================================

        if (dto.getAddresses() != null) {

            dto.getAddresses().forEach(addressDTO -> {

                PatientAddress address =
                        patientMapper.toAddressEntity(addressDTO);

                patient.addAddress(address);
            });
        }

        // ========================================================
        // ALLERGIES
        // ========================================================

        if (dto.getAllergies() != null) {

            dto.getAllergies().forEach(allergyDTO -> {

                PatientAllergy allergy =
                        patientMapper.toAllergyEntity(allergyDTO);

                patient.addAllergy(allergy);
            });
        }

        // Save patient
        Patient saved = patientRepository.save(patient);

        log.info(
                "Patient registered successfully with UHID: {}",
                saved.getUhid()
        );

        // Publish Kafka event
        publishPatientRegisteredEvent(saved);

        return patientMapper.toResponseDTO(saved);
    }

    // ============================================================
    // GET PATIENT BY UHID
    // ============================================================

    @Override
    @Cacheable(value = "patients", key = "#uhid")
    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientByUhid(String uhid) {

        Patient patient =
                patientRepository
                        .findByUhidAndDeletedAtIsNull(uhid)
                        .orElseThrow(
                                () -> new PatientNotFoundException(uhid)
                        );

        return patientMapper.toResponseDTO(patient);
    }

    // ============================================================
    // GET PATIENT BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientById(UUID id) {

        Patient patient =
                patientRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new PatientNotFoundException(
                                        id.toString()
                                )
                        );

        return patientMapper.toResponseDTO(patient);
    }

    // ============================================================
    // UPDATE PATIENT
    // ============================================================

    @Override
    @CacheEvict(value = "patients", key = "#uhid")
    @Transactional
    public PatientResponseDTO updatePatient(
            String uhid,
            PatientUpdateDTO dto
    ) {

        Patient patient =
                patientRepository
                        .findByUhidAndDeletedAtIsNull(uhid)
                        .orElseThrow(
                                () -> new PatientNotFoundException(uhid)
                        );

        patientMapper.updateEntity(dto, patient);

        Patient updated =
                patientRepository.save(patient);

        log.info(
                "Patient updated successfully: {}",
                uhid
        );

        return patientMapper.toResponseDTO(updated);
    }

    // ============================================================
    // SEARCH PATIENTS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<PatientResponseDTO> searchPatients(
            PatientSearchCriteria criteria,
            Pageable pageable
    ) {

        Page<Patient> page;

        if (criteria.getUhid() != null) {

            page =
                    patientRepository
                            .findByUhidAndDeletedAtIsNull(
                                    criteria.getUhid()
                            )
                            .map(patient ->
                                    (Page<Patient>)
                                            new org.springframework.data.domain.PageImpl<>(
                                                    List.of(patient)
                                            )
                            )
                            .orElse(Page.empty());

        } else if (criteria.getName() != null) {

            page =
                    patientRepository.searchByName(
                            criteria.getName(),
                            pageable
                    );

        } else if (criteria.getPhone() != null) {

            page =
                    patientRepository.searchByPhone(
                            criteria.getPhone(),
                            pageable
                    );

        } else {

            page =
                    patientRepository.findAll(pageable);
        }

        return PagedResponse.from(
                page.map(patientMapper::toResponseDTO)
        );
    }

    // ============================================================
    // SOFT DELETE PATIENT
    // ============================================================

    @Override
    @CacheEvict(value = "patients", key = "#uhid")
    @Transactional
    public void softDeletePatient(String uhid) {

        Patient patient =
                patientRepository
                        .findByUhidAndDeletedAtIsNull(uhid)
                        .orElseThrow(
                                () -> new PatientNotFoundException(uhid)
                        );

        patient.softDelete();

        patient.setStatus(
                Patient.PatientStatus.INACTIVE
        );

        patientRepository.save(patient);

        log.info(
                "Patient soft-deleted successfully: {}",
                uhid
        );
    }

    // ============================================================
    // VISIT MANAGEMENT
    // ============================================================

    @Override
    @Transactional
    public VisitResponseDTO createVisit(
            String uhid,
            VisitCreateDTO dto
    ) {

        Patient patient =
                patientRepository
                        .findByUhidAndDeletedAtIsNull(uhid)
                        .orElseThrow(
                                () -> new PatientNotFoundException(uhid)
                        );

        Visit visit =
                Visit.builder()
                        .patient(patient)
                        .visitType(
                                Visit.VisitType.valueOf(
                                        dto.getVisitType()
                                )
                        )
                        .departmentCode(
                                dto.getDepartmentCode()
                        )
                        .doctorId(
                                dto.getDoctorId()
                        )
                        .doctorName(
                                dto.getDoctorName()
                        )
                        .status(
                                Visit.VisitStatus.REGISTERED
                        )
                        .notes(
                                dto.getNotes()
                        )
                        .build();

        Visit saved =
                visitRepository.save(visit);

        log.info(
                "Visit created for patient {}: type={}",
                uhid,
                dto.getVisitType()
        );

        // ========================================================
        // PUBLISH VISIT EVENT
        // ========================================================

        PatientVisitCreatedEvent event =
                PatientVisitCreatedEvent.builder()
                        .visitId(
                                saved.getId().toString()
                        )
                        .uhid(uhid)
                        .patientName(
                                patient.getFullName()
                        )
                        .visitType(
                                dto.getVisitType()
                        )
                        .departmentCode(
                                dto.getDepartmentCode()
                        )
                        .doctorId(
                                dto.getDoctorId()
                        )
                        .doctorName(
                                dto.getDoctorName()
                        )
                        .build();

        event.initDefaults(
                "PATIENT_VISIT_CREATED",
                "hm-patient-service"
        );

        eventPublisher.publish(
                KafkaTopics.PATIENT_VISIT_CREATED,
                uhid,
                event
        );

        return patientMapper.toVisitResponseDTO(saved);
    }

    // ============================================================
    // VISIT HISTORY
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<VisitResponseDTO> getVisitHistory(
            String uhid,
            Pageable pageable
    ) {

        Page<Visit> visits =
                visitRepository
                        .findByPatientUhidOrderByCreatedAtDesc(
                                uhid,
                                pageable
                        );

        return PagedResponse.from(
                visits.map(
                        patientMapper::toVisitResponseDTO
                )
        );
    }

    // ============================================================
    // KAFKA - PATIENT REGISTERED EVENT
    // ============================================================

    private void publishPatientRegisteredEvent(
            Patient patient
    ) {

        PatientRegisteredEvent event =
                PatientRegisteredEvent.builder()
                        .uhid(
                                patient.getUhid()
                        )
                        .firstName(
                                patient.getFirstName()
                        )
                        .lastName(
                                patient.getLastName()
                        )
                        .dateOfBirth(
                                patient.getDateOfBirth()
                        )
                        .gender(
                                patient.getGender().name()
                        )
                        .primaryPhone(
                                patient.getPrimaryPhone()
                        )
                        .email(
                                patient.getEmail()
                        )
                        .build();

        event.initDefaults(
                "PATIENT_REGISTERED",
                "hm-patient-service"
        );

        eventPublisher.publish(
                KafkaTopics.PATIENT_REGISTERED,
                patient.getUhid(),
                event
        );
    }
}