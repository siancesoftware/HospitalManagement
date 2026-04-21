CREATE TABLE doctor_schedules (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    doctor_id       VARCHAR(100) NOT NULL,
    day_of_week     INTEGER NOT NULL CHECK (day_of_week BETWEEN 0 AND 6),
    start_time      TIME NOT NULL,
    end_time        TIME NOT NULL,
    slot_duration_min INTEGER NOT NULL DEFAULT 15,
    max_patients    INTEGER NOT NULL DEFAULT 30,
    location_code   VARCHAR(50),
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE,
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP WITH TIME ZONE,
    version         BIGINT DEFAULT 0,
    UNIQUE(doctor_id, day_of_week)
);

CREATE TABLE schedule_overrides (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    doctor_id       VARCHAR(100) NOT NULL,
    override_date   DATE NOT NULL,
    override_type   VARCHAR(20) NOT NULL,
    modified_slots  JSONB,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE,
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP WITH TIME ZONE,
    version         BIGINT DEFAULT 0
);
CREATE INDEX idx_override_doctor_date ON schedule_overrides(doctor_id, override_date);

CREATE TABLE appointments (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_uhid    VARCHAR(20) NOT NULL,
    doctor_id       VARCHAR(100) NOT NULL,
    appointment_date DATE NOT NULL,
    slot_time       TIME NOT NULL,
    appointment_type VARCHAR(30) NOT NULL DEFAULT 'REGULAR',
    status          VARCHAR(20) NOT NULL DEFAULT 'BOOKED',
    source          VARCHAR(30) DEFAULT 'WALK_IN',
    cancellation_reason TEXT,
    booked_by       VARCHAR(100),
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE,
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP WITH TIME ZONE,
    version         BIGINT DEFAULT 0
);
CREATE INDEX idx_appt_patient ON appointments(patient_uhid);
CREATE INDEX idx_appt_doctor_date ON appointments(doctor_id, appointment_date);
CREATE INDEX idx_appt_status ON appointments(status);

CREATE TABLE tokens (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    appointment_id  UUID REFERENCES appointments(id),
    department_code VARCHAR(50) NOT NULL,
    token_number    INTEGER NOT NULL,
    counter         INTEGER DEFAULT 1,
    status          VARCHAR(20) NOT NULL DEFAULT 'WAITING',
    called_at       TIMESTAMP WITH TIME ZONE,
    seen_at         TIMESTAMP WITH TIME ZONE,
    completed_at    TIMESTAMP WITH TIME ZONE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE,
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP WITH TIME ZONE,
    version         BIGINT DEFAULT 0
);
CREATE INDEX idx_token_dept_status ON tokens(department_code, status);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL, action VARCHAR(30) NOT NULL, user_id VARCHAR(100),
    username VARCHAR(100), ip_address VARCHAR(50), service_name VARCHAR(100),
    old_value JSONB, new_value JSONB, metadata JSONB, timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id);
