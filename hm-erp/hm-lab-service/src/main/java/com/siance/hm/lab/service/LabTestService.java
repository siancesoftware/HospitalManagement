package com.siance.hm.lab.service;

import com.siance.hm.lab.dto.LabTestRequest;
import com.siance.hm.lab.dto.LabTestResponse;

import java.util.List;
import java.util.UUID;

public interface LabTestService {

    LabTestResponse create(
            LabTestRequest request,
            UUID hospitalId
    );

    List<LabTestResponse> search(
            String query,
            UUID hospitalId
    );

    LabTestResponse update(
            UUID id,
            LabTestRequest request,
            UUID hospitalId
    );
}