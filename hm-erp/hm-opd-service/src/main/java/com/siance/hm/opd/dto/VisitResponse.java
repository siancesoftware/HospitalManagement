package com.siance.hm.opd.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record VisitResponse(
        UUID id, UUID patientId, UUID hospitalId, String visitType, UUID departmentId, UUID doctorId,
        String status, Integer tokenNumber, String chiefComplaint, OffsetDateTime startedAt,
        OffsetDateTime endedAt, OffsetDateTime createdAt
) {
}
