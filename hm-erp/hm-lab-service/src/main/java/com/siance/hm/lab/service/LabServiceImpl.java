package com.siance.hm.lab.service.impl;

import com.siance.hm.common.exception.ResourceNotFoundException;
import com.siance.hm.lab.dto.LabRequest;
import com.siance.hm.lab.dto.LabResponse;
import com.siance.hm.lab.entity.Lab;
import com.siance.hm.lab.repository.LabRepository;
import com.siance.hm.lab.service.LabService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LabServiceImpl implements LabService {

    private final LabRepository labRepository;

    @Override
    @Transactional
    public LabResponse create(LabRequest request, UUID hospitalId) {
        Lab lab = new Lab();
        lab.setName(request.getName());
        lab.setLocation(request.getLocation());
        lab.setHospitalId(hospitalId);
        return toResponse(labRepository.save(lab));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResponse> list(UUID hospitalId) {
        return labRepository.findByHospitalId(hospitalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LabResponse getById(UUID id, UUID hospitalId) {
        return toResponse(find(id, hospitalId));
    }

    private Lab find(UUID id, UUID hospitalId) {
        return labRepository.findByIdAndHospitalId(id, hospitalId)
                .orElseThrow(() -> ResourceNotFoundException.of("Lab", id));
    }

    private LabResponse toResponse(Lab l) {
        return new LabResponse(
                l.getId(),
                l.getName(),
                l.getLocation(),
                l.isActive(),
                l.getHospitalId()
        );
    }
}