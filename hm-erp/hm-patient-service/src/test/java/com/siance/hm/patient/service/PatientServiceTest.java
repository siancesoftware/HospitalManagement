package com.siance.hm.patient.service;

import com.siance.hm.kafka.producer.EventPublisher;
import com.siance.hm.patient.dto.*;
import com.siance.hm.patient.entity.Patient;
import com.siance.hm.patient.exception.PatientNotFoundException;
import com.siance.hm.patient.mapper.PatientMapper;
import com.siance.hm.patient.repository.PatientRepository;
import com.siance.hm.patient.repository.VisitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock private PatientRepository patientRepository;
    @Mock private VisitRepository visitRepository;
    @Mock private PatientMapper patientMapper;
    @Mock private EventPublisher eventPublisher;
    @InjectMocks private PatientService patientService;

    @Test
    void getPatientByUhid_WhenExists_ReturnsPatient() {
        String uhid = "HM-2026-000001";
        Patient patient = Patient.builder().uhid(uhid).firstName("John").lastName("Doe").build();
        patient.setId(UUID.randomUUID());
        PatientResponseDTO dto = PatientResponseDTO.builder().uhid(uhid).firstName("John").lastName("Doe").build();

        when(patientRepository.findByUhidAndDeletedAtIsNull(uhid)).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(patient)).thenReturn(dto);

        PatientResponseDTO result = patientService.getPatientByUhid(uhid);
        assertEquals(uhid, result.getUhid());
        assertEquals("John", result.getFirstName());
    }

    @Test
    void getPatientByUhid_WhenNotExists_ThrowsException() {
        when(patientRepository.findByUhidAndDeletedAtIsNull("INVALID")).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientByUhid("INVALID"));
    }
}
