package com.siance.hm.opd.dto;

import com.siance.hm.opd.entity.VisitStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVisitStatusRequest {
    @NotNull
    private VisitStatus status;
}
