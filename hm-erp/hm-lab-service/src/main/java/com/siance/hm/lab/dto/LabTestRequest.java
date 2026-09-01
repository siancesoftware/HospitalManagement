package com.siance.hm.lab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LabTestRequest {
    @NotBlank
    private String testCode;
    @NotBlank
    private String name;
    private String loincCode;
    private String section;
    private String sampleType;
    private Integer tatHours;
    private BigDecimal price;
}
