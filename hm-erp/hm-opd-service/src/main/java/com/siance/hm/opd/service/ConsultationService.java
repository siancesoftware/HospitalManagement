package com.siance.hm.opd.service;

import com.siance.hm.opd.dto.ConsultationRequest;
import com.siance.hm.opd.dto.ConsultationResponse;

import java.util.UUID;

public interface ConsultationService {

    ConsultationResponse createOrUpdate(
            ConsultationRequest request,
            UUID doctorId,
            UUID hospitalId
    );

    ConsultationResponse getByVisit(
            UUID visitId,
            UUID hospitalId
    );
}