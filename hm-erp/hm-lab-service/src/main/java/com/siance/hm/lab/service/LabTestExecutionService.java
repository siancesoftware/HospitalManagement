package com.siance.hm.lab.service;

import com.siance.hm.common.response.PagedResult;
import com.siance.hm.common.web.PageQuery;
import com.siance.hm.lab.dto.LabTestExecutionResponse;
import com.siance.hm.lab.dto.OrderLabTestRequest;

import java.util.List;
import java.util.UUID;

public interface LabTestExecutionService {

    LabTestExecutionResponse order(
            OrderLabTestRequest request,
            UUID orderedBy,
            UUID hospitalId
    );

    LabTestExecutionResponse collectSample(
            UUID id,
            String barcode,
            UUID hospitalId
    );

    LabTestExecutionResponse enterResult(
            UUID id,
            String resultValuesJson,
            UUID hospitalId
    );

    LabTestExecutionResponse validate(
            UUID id,
            UUID validatedBy,
            UUID hospitalId
    );

    LabTestExecutionResponse getById(
            UUID id,
            UUID hospitalId
    );

    PagedResult<LabTestExecutionResponse> list(
            PageQuery query,
            UUID hospitalId
    );

    List<LabTestExecutionResponse> forPatient(
            UUID patientId,
            UUID hospitalId
    );
}