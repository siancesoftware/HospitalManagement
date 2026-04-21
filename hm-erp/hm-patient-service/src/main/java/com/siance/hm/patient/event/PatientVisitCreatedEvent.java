package com.siance.hm.patient.event;

import com.siance.hm.common.event.BaseEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data @SuperBuilder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PatientVisitCreatedEvent extends BaseEvent {
    private String visitId;
    private String uhid;
    private String patientName;
    private String visitType;
    private String departmentCode;
    private String doctorId;
    private String doctorName;
}
