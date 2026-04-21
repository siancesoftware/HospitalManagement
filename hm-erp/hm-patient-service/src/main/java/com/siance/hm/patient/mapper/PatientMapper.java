package com.siance.hm.patient.mapper;

import com.siance.hm.patient.dto.*;
import com.siance.hm.patient.entity.*;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uhid", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "allergies", ignore = true)
    @Mapping(target = "visits", ignore = true)
    Patient toEntity(PatientCreateDTO dto);

    @Mapping(target = "fullName", expression = "java(patient.getFullName())")
    PatientResponseDTO toResponseDTO(Patient patient);

    List<PatientResponseDTO> toResponseDTOs(List<Patient> patients);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uhid", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "allergies", ignore = true)
    @Mapping(target = "visits", ignore = true)
    void updateEntity(PatientUpdateDTO dto, @MappingTarget Patient patient);

    ContactDTO toContactDTO(PatientContact contact);
    PatientContact toContactEntity(ContactDTO dto);
    AddressDTO toAddressDTO(PatientAddress address);
    PatientAddress toAddressEntity(AddressDTO dto);
    AllergyDTO toAllergyDTO(PatientAllergy allergy);
    PatientAllergy toAllergyEntity(AllergyDTO dto);

    @Mapping(target = "patientUhid", source = "patient.uhid")
    VisitResponseDTO toVisitResponseDTO(Visit visit);
}
