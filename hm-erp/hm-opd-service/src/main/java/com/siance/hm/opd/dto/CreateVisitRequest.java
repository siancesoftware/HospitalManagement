package com.siance.hm.opd.dto;

import com.siance.hm.opd.entity.VisitType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateVisitRequest {
    @NotNull
    private UUID patientId;
    private UUID departmentId;
    private UUID doctorId;
    private VisitType visitType = VisitType.OPD;
    private String chiefComplaint;
}
