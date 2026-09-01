package com.siance.hm.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Bound from {@code hm.jwt.*}. Each service's application.yml maps these to
 * the same environment variable names the original {@code .env} used
 * (JWT_SECRET, JWT_EXPIRATION_TIME, JWT_REFRESH_SECRET,
 * JWT_REFRESH_EXPIRATION_TIME) so existing deployment secrets/CI variables
 * carry over unchanged.
 *
 * <p>HS256 requires a key of at least 256 bits (32 UTF-8 characters) - the
 * defaults below are long enough for local development only. Always
 * override in production.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "hm.jwt")
public class JwtProperties {

    /** Signing secret for access tokens. Must be >= 32 characters. */
    private String secret = "dev-only-access-secret-change-me-please-32b!!";

    /** Access token lifetime, in seconds. Original default: 3600 (1 hour). */
    private long expirationSeconds = 3600;

    /** Signing secret for refresh tokens - deliberately different from {@link #secret}. */
    private String refreshSecret = "dev-only-refresh-secret-change-me-please-32b!!";

    /** Refresh token lifetime, in seconds. Original default: 604800 (7 days). */
    private long refreshExpirationSeconds = 604_800;

    private String issuer = "hm-erp";
}
