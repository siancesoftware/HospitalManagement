package com.siance.hm.lab.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LabTestExecutionResponse(
        UUID id, UUID labId, UUID testId, String testName, UUID patientId, UUID visitId, String status,
        String sampleBarcode, OffsetDateTime sampleCollectedAt, String resultValuesJson,
        OffsetDateTime resultEnteredAt, OffsetDateTime validatedAt, String reportUrl
) {
}
