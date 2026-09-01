package com.siance.hm.opd.service;

import com.siance.hm.common.response.PagedResult;
import com.siance.hm.common.web.PageQuery;
import com.siance.hm.opd.dto.CreateVisitRequest;
import com.siance.hm.opd.dto.VisitResponse;
import com.siance.hm.opd.entity.VisitStatus;

import java.util.List;
import java.util.UUID;

public interface VisitService {

    VisitResponse create(
            CreateVisitRequest request,
            UUID hospitalId
    );

    VisitResponse getById(
            UUID id,
            UUID hospitalId
    );

    PagedResult<VisitResponse> list(
            PageQuery query,
            UUID hospitalId
    );

    List<VisitResponse> forPatient(
            UUID patientId,
            UUID hospitalId
    );

    VisitResponse updateStatus(
            UUID id,
            VisitStatus status,
            UUID hospitalId
    );
}