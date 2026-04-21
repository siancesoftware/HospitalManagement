-- UHID Sequence
CREATE SEQUENCE IF NOT EXISTS uhid_sequence START WITH 1 INCREMENT BY 1;

-- Patients table
CREATE TABLE patients (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    uhid            VARCHAR(20) NOT NULL UNIQUE,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    date_of_birth   DATE,
    gender          VARCHAR(20) NOT NULL,
    blood_group     VARCHAR(10),
    marital_status  VARCHAR(20),
    nationality     VARCHAR(50),
    national_id     VARCHAR(50),
    primary_phone   VARCHAR(20),
    email           VARCHAR(150),
    photo_url       TEXT,
    referral_source VARCHAR(50),
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    metadata        JSONB,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE,
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP WITH TIME ZONE,
    version         BIGINT DEFAULT 0
);

CREATE INDEX idx_patient_uhid ON patients(uhid);
CREATE INDEX idx_patient_national_id ON patients(national_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_patient_phone ON patients(primary_phone) WHERE deleted_at IS NULL;
CREATE INDEX idx_patient_name ON patients(first_name, last_name) WHERE deleted_at IS NULL;
CREATE INDEX idx_patient_status ON patients(status) WHERE deleted_at IS NULL;

-- Patient Contacts
CREATE TABLE patient_contacts (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id      UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    contact_type    VARCHAR(30) NOT NULL,
    contact_value   VARCHAR(150) NOT NULL,
    contact_name    VARCHAR(100),
    relationship    VARCHAR(50),
    is_primary      BOOLEAN DEFAULT FALSE,
    is_emergency    BOOLEAN DEFAULT FALSE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE,
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP WITH TIME ZONE,
    version         BIGINT DEFAULT 0
);

CREATE INDEX idx_contact_patient ON patient_contacts(patient_id);

-- Patient Addresses
CREATE TABLE patient_addresses (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id      UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    address_type    VARCHAR(20),
    line1           TEXT NOT NULL,
    line2           TEXT,
    city            VARCHAR(100),
    state           VARCHAR(100),
    country         VARCHAR(100) DEFAULT 'India',
    pin_code        VARCHAR(10),
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE,
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP WITH TIME ZONE,
    version         BIGINT DEFAULT 0
);

CREATE INDEX idx_address_patient ON patient_addresses(patient_id);

-- Patient Allergies
CREATE TABLE patient_allergies (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id      UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    allergen        VARCHAR(200) NOT NULL,
    allergy_type    VARCHAR(30),
    severity        VARCHAR(20),
    reaction        VARCHAR(500),
    verified_by     VARCHAR(100),
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE,
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP WITH TIME ZONE,
    version         BIGINT DEFAULT 0
);

CREATE INDEX idx_allergy_patient ON patient_allergies(patient_id);

-- Visits
CREATE TABLE visits (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id      UUID NOT NULL REFERENCES patients(id),
    visit_type      VARCHAR(20) NOT NULL,
    department_code VARCHAR(50),
    doctor_id       VARCHAR(100),
    doctor_name     VARCHAR(200),
    status          VARCHAR(20) NOT NULL DEFAULT 'REGISTERED',
    token_number    INTEGER,
    started_at      TIMESTAMP WITH TIME ZONE,
    ended_at        TIMESTAMP WITH TIME ZONE,
    notes           TEXT,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE,
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    deleted_at      TIMESTAMP WITH TIME ZONE,
    version         BIGINT DEFAULT 0
);

CREATE INDEX idx_visit_patient ON visits(patient_id);
CREATE INDEX idx_visit_doctor ON visits(doctor_id);
CREATE INDEX idx_visit_type_status ON visits(visit_type, status);
CREATE INDEX idx_visit_created ON visits(created_at);

-- Audit Logs
CREATE TABLE audit_logs (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_type     VARCHAR(100) NOT NULL,
    entity_id       VARCHAR(100) NOT NULL,
    action          VARCHAR(30) NOT NULL,
    user_id         VARCHAR(100),
    username        VARCHAR(100),
    ip_address      VARCHAR(50),
    service_name    VARCHAR(100),
    old_value       JSONB,
    new_value       JSONB,
    metadata        JSONB,
    timestamp       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_user ON audit_logs(user_id);
CREATE INDEX idx_audit_timestamp ON audit_logs(timestamp);
