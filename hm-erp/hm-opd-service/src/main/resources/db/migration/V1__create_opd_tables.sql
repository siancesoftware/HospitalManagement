CREATE TABLE opd_visits (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    visit_id        VARCHAR(100),
    patient_uhid    VARCHAR(20) NOT NULL,
    doctor_id       VARCHAR(100) NOT NULL,
    department      VARCHAR(100),
    chief_complaint TEXT,
    vitals_snapshot JSONB,
    status          VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE,
    created_by      VARCHAR(100), updated_by VARCHAR(100),
    deleted_at      TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);
CREATE INDEX idx_opd_visit ON opd_visits(visit_id);
CREATE INDEX idx_opd_patient ON opd_visits(patient_uhid);

CREATE TABLE clinical_notes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), opd_visit_id UUID NOT NULL REFERENCES opd_visits(id),
    note_type VARCHAR(20) NOT NULL DEFAULT 'SOAP', subjective TEXT, objective TEXT, assessment TEXT, plan TEXT,
    created_by VARCHAR(100), created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE, updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);

CREATE TABLE diagnoses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), opd_visit_id UUID NOT NULL REFERENCES opd_visits(id),
    icd_code VARCHAR(20) NOT NULL, description TEXT, diagnosis_type VARCHAR(30) DEFAULT 'PRIMARY',
    certainty VARCHAR(20) DEFAULT 'CONFIRMED',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);
CREATE INDEX idx_diag_icd ON diagnoses(icd_code);

CREATE TABLE prescriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), opd_visit_id UUID NOT NULL REFERENCES opd_visits(id),
    doctor_id VARCHAR(100), status VARCHAR(20) DEFAULT 'CREATED', pharmacy_status VARCHAR(20) DEFAULT 'PENDING',
    notes TEXT, created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);

CREATE TABLE prescription_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), prescription_id UUID NOT NULL REFERENCES prescriptions(id),
    drug_code VARCHAR(50) NOT NULL, drug_name VARCHAR(200), dose VARCHAR(50), frequency VARCHAR(50),
    route VARCHAR(30), duration_days INTEGER, quantity INTEGER, instructions TEXT,
    substitution_allowed BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);

CREATE TABLE investigation_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), opd_visit_id UUID NOT NULL REFERENCES opd_visits(id),
    test_code VARCHAR(50) NOT NULL, test_name VARCHAR(200), urgency VARCHAR(20) DEFAULT 'ROUTINE',
    clinical_info TEXT, status VARCHAR(20) DEFAULT 'ORDERED', result_reference VARCHAR(200),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);

CREATE TABLE referrals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), opd_visit_id UUID NOT NULL REFERENCES opd_visits(id),
    from_doctor VARCHAR(100), to_doctor_or_dept VARCHAR(200), reason TEXT,
    urgency VARCHAR(20) DEFAULT 'ROUTINE', clinical_summary TEXT, status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL, action VARCHAR(30) NOT NULL, user_id VARCHAR(100),
    username VARCHAR(100), ip_address VARCHAR(50), service_name VARCHAR(100),
    old_value JSONB, new_value JSONB, metadata JSONB, timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id);
