CREATE TABLE notification_templates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), template_code VARCHAR(50) NOT NULL UNIQUE,
    channel VARCHAR(20) NOT NULL, subject VARCHAR(500), body_template TEXT NOT NULL,
    variables JSONB, is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), template_code VARCHAR(50),
    channel VARCHAR(20) NOT NULL, recipient VARCHAR(200) NOT NULL,
    subject VARCHAR(500), body TEXT, status VARCHAR(20) DEFAULT 'PENDING',
    sent_at TIMESTAMP WITH TIME ZONE, error_message TEXT, retry_count INTEGER DEFAULT 0,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), updated_at TIMESTAMP WITH TIME ZONE
);
CREATE INDEX idx_notification_status ON notifications(status);
CREATE INDEX idx_notification_channel ON notifications(channel, status);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL, action VARCHAR(30) NOT NULL, user_id VARCHAR(100),
    username VARCHAR(100), ip_address VARCHAR(50), service_name VARCHAR(100),
    old_value JSONB, new_value JSONB, metadata JSONB, timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id);
