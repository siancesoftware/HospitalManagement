CREATE TABLE charge_master (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), service_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL, cpt_code VARCHAR(20), department VARCHAR(100),
    base_rate DECIMAL(12,2) NOT NULL, tax_percent DECIMAL(5,2) DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE, effective_from DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);

CREATE TABLE tariffs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), name VARCHAR(100) NOT NULL,
    tariff_type VARCHAR(30) NOT NULL, is_default BOOLEAN DEFAULT FALSE,
    effective_from DATE NOT NULL DEFAULT CURRENT_DATE, status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);

CREATE TABLE tariff_rates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), tariff_id UUID NOT NULL REFERENCES tariffs(id),
    service_code VARCHAR(50) NOT NULL, rate DECIMAL(12,2) NOT NULL, discount_percent DECIMAL(5,2) DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);

CREATE SEQUENCE invoice_number_seq START WITH 1000;

CREATE TABLE invoices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), invoice_number VARCHAR(30) NOT NULL UNIQUE,
    patient_uhid VARCHAR(20) NOT NULL, visit_id VARCHAR(100), admission_id VARCHAR(100),
    invoice_type VARCHAR(10) NOT NULL, subtotal DECIMAL(14,2), tax DECIMAL(12,2),
    discount DECIMAL(12,2) DEFAULT 0, net_amount DECIMAL(14,2) NOT NULL,
    paid_amount DECIMAL(14,2) DEFAULT 0, balance DECIMAL(14,2),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);
CREATE INDEX idx_invoice_patient ON invoices(patient_uhid);
CREATE INDEX idx_invoice_status ON invoices(status);

CREATE TABLE invoice_line_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), invoice_id UUID NOT NULL REFERENCES invoices(id),
    service_code VARCHAR(50), description TEXT, quantity INTEGER DEFAULT 1,
    unit_rate DECIMAL(12,2), amount DECIMAL(12,2), tax DECIMAL(10,2) DEFAULT 0,
    discount DECIMAL(10,2) DEFAULT 0, department VARCHAR(100), doctor_id VARCHAR(100),
    source_module VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);

CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), invoice_id UUID NOT NULL REFERENCES invoices(id),
    amount DECIMAL(14,2) NOT NULL, payment_mode VARCHAR(30) NOT NULL,
    reference_number VARCHAR(100), gateway_txn_id VARCHAR(200),
    collected_by VARCHAR(100), collected_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    status VARCHAR(20) DEFAULT 'SUCCESS',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);

CREATE TABLE advances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), patient_uhid VARCHAR(20) NOT NULL,
    admission_id VARCHAR(100), amount DECIMAL(14,2) NOT NULL, payment_mode VARCHAR(30),
    reference_number VARCHAR(100), status VARCHAR(20) DEFAULT 'ACTIVE',
    adjusted_against_invoice_id UUID REFERENCES invoices(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100), updated_by VARCHAR(100), deleted_at TIMESTAMP WITH TIME ZONE, version BIGINT DEFAULT 0
);

CREATE TABLE refunds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), invoice_id UUID REFERENCES invoices(id),
    payment_id UUID REFERENCES payments(id), amount DECIMAL(14,2) NOT NULL,
    reason TEXT, approved_by VARCHAR(100), processed_at TIMESTAMP WITH TIME ZONE,
    refund_mode VARCHAR(30), status VARCHAR(20) DEFAULT 'PENDING',
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
