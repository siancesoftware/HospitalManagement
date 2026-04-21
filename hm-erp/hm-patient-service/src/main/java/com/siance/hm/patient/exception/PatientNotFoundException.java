package com.siance.hm.patient.exception;

import com.siance.hm.common.exception.ResourceNotFoundException;

public class PatientNotFoundException extends ResourceNotFoundException {
    public PatientNotFoundException(String uhid) {
        super("Patient", "UHID", uhid);
    }
}
