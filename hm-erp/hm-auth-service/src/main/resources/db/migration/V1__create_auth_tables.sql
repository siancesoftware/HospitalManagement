-- hm-auth-service / auth_db
-- Port of the User / Role / UserRole models from the original schema.prisma.

CREATE TABLE users
(
    id                   UUID PRIMARY KEY,
    first_name           VARCHAR(100) NOT NULL,
    last_name            VARCHAR(100) NOT NULL,
    email                VARCHAR(255) NOT NULL,
    password_hash        VARCHAR(255) NOT NULL,
    password_changed_at  TIMESTAMPTZ,
    status               VARCHAR(30)  NOT NULL DEFAULT 'ACTIVE',
    mfa_enabled          BOOLEAN      NOT NULL DEFAULT FALSE,
    failed_attempts      INT          NOT NULL DEFAULT 0,
    locked_until         TIMESTAMPTZ,
    last_login_at        TIMESTAMPTZ,
    is_active            BOOLEAN      NOT NULL DEFAULT TRUE,
    refresh_token_hash   VARCHAR(255),
    created_at           TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at           TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE INDEX idx_users_email ON users (email);

CREATE TABLE roles
(
    id          UUID PRIMARY KEY,
    code        VARCHAR(50)  NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    is_system   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT uq_roles_code UNIQUE (code)
);

CREATE TABLE user_roles
(
    id         UUID PRIMARY KEY,
    user_id    UUID         NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role_id    UUID         NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    scope_type VARCHAR(30),
    scope_id   VARCHAR(50),
    granted_by UUID,
    granted_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    expires_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT uq_user_roles UNIQUE (user_id, role_id, scope_type, scope_id)
);

CREATE INDEX idx_user_roles_user_id ON user_roles (user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles (role_id);
