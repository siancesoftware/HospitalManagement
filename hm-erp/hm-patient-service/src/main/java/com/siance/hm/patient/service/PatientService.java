package com.siance.hm.patient.service;

import com.siance.hm.common.dto.PagedResponse;
import com.siance.hm.patient.dto.PatientCreateDTO;
import com.siance.hm.patient.dto.PatientResponseDTO;
import com.siance.hm.patient.dto.PatientSearchCriteria;
import com.siance.hm.patient.dto.PatientUpdateDTO;
import com.siance.hm.patient.dto.VisitCreateDTO;
import com.siance.hm.patient.dto.VisitResponseDTO;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PatientService {

    PatientResponseDTO registerPatient(PatientCreateDTO dto);

    PatientResponseDTO getPatientByUhid(String uhid);

    PatientResponseDTO getPatientById(UUID id);

    PatientResponseDTO updatePatient(String uhid, PatientUpdateDTO dto);

    PagedResponse<PatientResponseDTO> searchPatients(
            PatientSearchCriteria criteria,
            Pageable pageable
    );

    void softDeletePatient(String uhid);

    VisitResponseDTO createVisit(
            String uhid,
            VisitCreateDTO dto
    );

    PagedResponse<VisitResponseDTO> getVisitHistory(
            String uhid,
            Pageable pageable
    );
}