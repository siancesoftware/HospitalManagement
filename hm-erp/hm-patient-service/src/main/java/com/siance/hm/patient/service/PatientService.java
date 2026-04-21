package com.siance.hm.patient.service;

import com.siance.hm.common.dto.PagedResponse;
import com.siance.hm.common.exception.DuplicateResourceException;
import com.siance.hm.common.util.UHIDGenerator;
import com.siance.hm.kafka.event.KafkaTopics;
import com.siance.hm.kafka.producer.EventPublisher;
import com.siance.hm.patient.dto.*;
import com.siance.hm.patient.entity.*;
import com.siance.hm.patient.event.*;
import com.siance.hm.patient.exception.PatientNotFoundException;
import com.siance.hm.patient.mapper.PatientMapper;
import com.siance.hm.patient.repository.PatientRepository;
import com.siance.hm.patient.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final PatientMapper patientMapper;
    private final EventPublisher eventPublisher;

    @Value("${app.uhid.prefix:HM}")
    private String uhidPrefix;

    @Transactional
    public PatientResponseDTO registerPatient(PatientCreateDTO dto) {
        log.info("Registering new patient: {} {}", dto.getFirstName(), dto.getLastName());

        // Duplicate check
        if (dto.getNationalId() != null && patientRepository.existsByNationalIdAndDeletedAtIsNull(dto.getNationalId())) {
            throw new DuplicateResourceException("Patient", "nationalId", dto.getNationalId());
        }

        Patient patient = patientMapper.toEntity(dto);

        // Generate UHID
        Long seq = patientRepository.getNextUhidSequence();
        patient.setUhid(UHIDGenerator.generate(uhidPrefix, seq));
        patient.setStatus(Patient.PatientStatus.ACTIVE);

        // Map nested collections
        if (dto.getContacts() != null) {
            dto.getContacts().forEach(c -> {
                PatientContact contact = patientMapper.toContactEntity(c);
                patient.addContact(contact);
            });
        }
        if (dto.getAddresses() != null) {
            dto.getAddresses().forEach(a -> {
                PatientAddress address = patientMapper.toAddressEntity(a);
                patient.addAddress(address);
            });
        }
        if (dto.getAllergies() != null) {
            dto.getAllergies().forEach(a -> {
                PatientAllergy allergy = patientMapper.toAllergyEntity(a);
                patient.addAllergy(allergy);
            });
        }

        Patient saved = patientRepository.save(patient);
        log.info("Patient registered with UHID: {}", saved.getUhid());

        // Publish event
        publishPatientRegisteredEvent(saved);

        return patientMapper.toResponseDTO(saved);
    }

    @Cacheable(value = "patients", key = "#uhid")
    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientByUhid(String uhid) {
        Patient patient = patientRepository.findByUhidAndDeletedAtIsNull(uhid)
                .orElseThrow(() -> new PatientNotFoundException(uhid));
        return patientMapper.toResponseDTO(patient);
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id.toString()));
        return patientMapper.toResponseDTO(patient);
    }

    @CacheEvict(value = "patients", key = "#uhid")
    @Transactional
    public PatientResponseDTO updatePatient(String uhid, PatientUpdateDTO dto) {
        Patient patient = patientRepository.findByUhidAndDeletedAtIsNull(uhid)
                .orElseThrow(() -> new PatientNotFoundException(uhid));

        patientMapper.updateEntity(dto, patient);
        Patient updated = patientRepository.save(patient);
        log.info("Patient updated: {}", uhid);
        return patientMapper.toResponseDTO(updated);
    }

    @Transactional(readOnly = true)
    public PagedResponse<PatientResponseDTO> searchPatients(PatientSearchCriteria criteria, Pageable pageable) {
        Page<Patient> page;
        if (criteria.getUhid() != null) {
            page = patientRepository.findByUhidAndDeletedAtIsNull(criteria.getUhid())
                    .map(p -> (Page<Patient>) new org.springframework.data.domain.PageImpl<>(java.util.List.of(p)))
                    .orElse(Page.empty());
        } else if (criteria.getName() != null) {
            page = patientRepository.searchByName(criteria.getName(), pageable);
        } else if (criteria.getPhone() != null) {
            page = patientRepository.searchByPhone(criteria.getPhone(), pageable);
        } else {
            page = patientRepository.findAll(pageable);
        }
        return PagedResponse.from(page.map(patientMapper::toResponseDTO));
    }

    @CacheEvict(value = "patients", key = "#uhid")
    @Transactional
    public void softDeletePatient(String uhid) {
        Patient patient = patientRepository.findByUhidAndDeletedAtIsNull(uhid)
                .orElseThrow(() -> new PatientNotFoundException(uhid));
        patient.softDelete();
        patient.setStatus(Patient.PatientStatus.INACTIVE);
        patientRepository.save(patient);
        log.info("Patient soft-deleted: {}", uhid);
    }

    // --- Visit Management ---
    @Transactional
    public VisitResponseDTO createVisit(String uhid, VisitCreateDTO dto) {
        Patient patient = patientRepository.findByUhidAndDeletedAtIsNull(uhid)
                .orElseThrow(() -> new PatientNotFoundException(uhid));

        Visit visit = Visit.builder()
                .patient(patient)
                .visitType(Visit.VisitType.valueOf(dto.getVisitType()))
                .departmentCode(dto.getDepartmentCode())
                .doctorId(dto.getDoctorId())
                .doctorName(dto.getDoctorName())
                .status(Visit.VisitStatus.REGISTERED)
                .notes(dto.getNotes())
                .build();

        Visit saved = visitRepository.save(visit);
        log.info("Visit created for patient {}: type={}", uhid, dto.getVisitType());

        // Publish visit event
        PatientVisitCreatedEvent event = PatientVisitCreatedEvent.builder()
                .visitId(saved.getId().toString())
                .uhid(uhid)
                .patientName(patient.getFullName())
                .visitType(dto.getVisitType())
                .departmentCode(dto.getDepartmentCode())
                .doctorId(dto.getDoctorId())
                .doctorName(dto.getDoctorName())
                .build();
        event.initDefaults("PATIENT_VISIT_CREATED", "hm-patient-service");
        eventPublisher.publish(KafkaTopics.PATIENT_VISIT_CREATED, uhid, event);

        return patientMapper.toVisitResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public PagedResponse<VisitResponseDTO> getVisitHistory(String uhid, Pageable pageable) {
        Page<Visit> visits = visitRepository.findByPatientUhidOrderByCreatedAtDesc(uhid, pageable);
        return PagedResponse.from(visits.map(patientMapper::toVisitResponseDTO));
    }

    private void publishPatientRegisteredEvent(Patient patient) {
        PatientRegisteredEvent event = PatientRegisteredEvent.builder()
                .uhid(patient.getUhid())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender().name())
                .primaryPhone(patient.getPrimaryPhone())
                .email(patient.getEmail())
                .build();
        event.initDefaults("PATIENT_REGISTERED", "hm-patient-service");
        eventPublisher.publish(KafkaTopics.PATIENT_REGISTERED, patient.getUhid(), event);
    }
}
