package com.siance.hm.patient.event;

import com.siance.hm.common.event.BaseEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@Data @SuperBuilder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PatientRegisteredEvent extends BaseEvent {
    private String uhid;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String primaryPhone;
    private String email;
}
