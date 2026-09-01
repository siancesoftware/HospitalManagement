package com.siance.hm.opd.dto;

import java.util.UUID;

public record ConsultationResponse(UUID id, UUID visitId, UUID patientId, UUID doctorId, String subjective,
                                    String objective, String assessment, String plan, String diagnosis) {
}
