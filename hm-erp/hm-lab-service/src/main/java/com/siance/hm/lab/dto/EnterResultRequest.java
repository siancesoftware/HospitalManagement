package com.siance.hm.lab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnterResultRequest {
    /** JSON object of parameter name -> value, e.g. {"Hemoglobin": "13.5 g/dL"}. */
    @NotBlank
    private String resultValuesJson;
}
