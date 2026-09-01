package com.siance.hm.lab.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record LabTestResponse(UUID id, String testCode, String name, String loincCode, String section,
                               String sampleType, Integer tatHours, BigDecimal price, boolean active) {
}
