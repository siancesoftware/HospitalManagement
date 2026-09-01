package com.siance.hm.lab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectSampleRequest {
    @NotBlank
    private String sampleBarcode;
}
