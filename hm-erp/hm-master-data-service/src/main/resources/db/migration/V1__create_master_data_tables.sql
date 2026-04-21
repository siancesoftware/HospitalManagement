CREATE TABLE icd_codes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), code VARCHAR(20) NOT NULL UNIQUE,
    description TEXT NOT NULL, category VARCHAR(200), chapter VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE
);
CREATE INDEX idx_icd_code ON icd_codes(code);

CREATE TABLE drug_master (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), drug_code VARCHAR(50) NOT NULL UNIQUE,
    generic_name VARCHAR(200) NOT NULL, brand_name VARCHAR(200), manufacturer VARCHAR(200),
    formulation VARCHAR(50), strength VARCHAR(50), unit VARCHAR(30),
    schedule_type VARCHAR(20), is_formulary BOOLEAN DEFAULT TRUE, is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);
CREATE INDEX idx_drug_generic ON drug_master(generic_name);

CREATE TABLE drug_interactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), drug_a_code VARCHAR(50) NOT NULL,
    drug_b_code VARCHAR(50) NOT NULL, severity VARCHAR(20) NOT NULL,
    description TEXT, clinical_action TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_interaction_drugs ON drug_interactions(drug_a_code, drug_b_code);

CREATE TABLE departments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL, parent_code VARCHAR(50), is_clinical BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE service_catalog (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), service_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL, department_code VARCHAR(50), category VARCHAR(100),
    base_price DECIMAL(12,2), is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL, action VARCHAR(30) NOT NULL, user_id VARCHAR(100),
    username VARCHAR(100), ip_address VARCHAR(50), service_name VARCHAR(100),
    old_value JSONB, new_value JSONB, metadata JSONB, timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id);
