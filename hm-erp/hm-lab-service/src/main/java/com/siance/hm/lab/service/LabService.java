package com.siance.hm.lab.service;

import com.siance.hm.lab.dto.LabRequest;
import com.siance.hm.lab.dto.LabResponse;

import java.util.List;
import java.util.UUID;

public interface LabService {

    LabResponse create(LabRequest request, UUID hospitalId);

    List<LabResponse> list(UUID hospitalId);

    LabResponse getById(UUID id, UUID hospitalId);
}