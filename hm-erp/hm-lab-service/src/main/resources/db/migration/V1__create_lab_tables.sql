-- hm-lab-service / lab_db
-- Port of Lab, LabStaff, LabTest, LabTestExecution, LabToken, LabTestBill,
-- LabTestBillItem, LabTestPayment.

CREATE TABLE labs
(
    id          UUID PRIMARY KEY,
    hospital_id UUID         NOT NULL,
    name        VARCHAR(150) NOT NULL,
    location    VARCHAR(100),
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE lab_staff
(
    id               UUID PRIMARY KEY,
    lab_id           UUID        NOT NULL REFERENCES labs (id) ON DELETE CASCADE,
    hospital_user_id UUID        NOT NULL,
    hospital_id      UUID        NOT NULL,
    role             VARCHAR(50),
    is_active        BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_lab_staff UNIQUE (lab_id, hospital_user_id)
);

CREATE TABLE lab_tests
(
    id              UUID PRIMARY KEY,
    hospital_id     UUID           NOT NULL,
    test_code       VARCHAR(30)    NOT NULL,
    name            VARCHAR(200)   NOT NULL,
    loinc_code      VARCHAR(30),
    section         VARCHAR(100),
    sample_type     VARCHAR(50),
    tat_hours       INT,
    price           NUMERIC(10, 2),
    reference_info  JSONB,
    is_active       BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ    NOT NULL DEFAULT now(),
    CONSTRAINT uq_lab_tests_hospital_code UNIQUE (hospital_id, test_code)
);

CREATE TABLE lab_test_executions
(
    id                        UUID PRIMARY KEY,
    lab_id                    UUID        NOT NULL REFERENCES labs (id),
    test_id                   UUID        NOT NULL REFERENCES lab_tests (id),
    hospital_id               UUID        NOT NULL,
    patient_id                UUID        NOT NULL,
    visit_id                  UUID,
    prescription_lab_test_ref VARCHAR(50),
    ordered_by                UUID,
    status                    VARCHAR(20) NOT NULL DEFAULT 'ORDERED',
    sample_barcode            VARCHAR(50),
    sample_collected_at       TIMESTAMPTZ,
    result_values             JSONB,
    result_entered_at         TIMESTAMPTZ,
    validated_by              UUID,
    validated_at              TIMESTAMPTZ,
    report_url                TEXT,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_lte_hospital_id ON lab_test_executions (hospital_id);
CREATE INDEX idx_lte_patient_id ON lab_test_executions (patient_id);

CREATE TABLE lab_tokens
(
    id           UUID PRIMARY KEY,
    lab_id       UUID        NOT NULL REFERENCES labs (id),
    hospital_id  UUID        NOT NULL,
    patient_id   UUID        NOT NULL,
    token_number INT         NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'WAITING',
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE lab_test_bills
(
    id           UUID PRIMARY KEY,
    hospital_id  UUID           NOT NULL,
    patient_id   UUID           NOT NULL,
    visit_id     UUID,
    bill_number  VARCHAR(30)    NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
    paid_amount  NUMERIC(12, 2) NOT NULL DEFAULT 0,
    status       VARCHAR(20)    NOT NULL DEFAULT 'UNPAID',
    created_at   TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ    NOT NULL DEFAULT now(),
    CONSTRAINT uq_lab_test_bills_number UNIQUE (bill_number)
);

CREATE TABLE lab_test_bill_items
(
    id                 UUID PRIMARY KEY,
    bill_id            UUID           NOT NULL REFERENCES lab_test_bills (id) ON DELETE CASCADE,
    test_execution_id  UUID,
    description        VARCHAR(200)   NOT NULL,
    amount             NUMERIC(10, 2) NOT NULL,
    created_at         TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at         TIMESTAMPTZ    NOT NULL DEFAULT now()
);

CREATE TABLE lab_test_payments
(
    id               UUID PRIMARY KEY,
    bill_id          UUID           NOT NULL REFERENCES lab_test_bills (id),
    amount           NUMERIC(10, 2) NOT NULL,
    payment_mode     VARCHAR(20)    NOT NULL,
    reference_number VARCHAR(100),
    paid_at          TIMESTAMPTZ    NOT NULL DEFAULT now(),
    created_at       TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ    NOT NULL DEFAULT now()
);
