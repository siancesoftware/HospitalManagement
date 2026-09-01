package com.siance.hm.lab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabRequest {
    @NotBlank
    private String name;
    private String location;
}
