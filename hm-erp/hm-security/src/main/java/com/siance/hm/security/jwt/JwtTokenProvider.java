package com.siance.hm.security.jwt;

import com.siance.hm.security.principal.AuthPrincipal;
import com.siance.hm.security.principal.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Issues and verifies access/refresh tokens for all four principal types.
 * Equivalent of the original {@code JwtStrategy} + the signing half of
 * {@code AuthService} - but stateless (see {@link AuthPrincipal} for why).
 *
 * <p>Access and refresh tokens are signed with two different secrets, same
 * as the original (JWT_SECRET vs JWT_REFRESH_SECRET), and each carries a
 * "tokenUse" claim so an access token can never be replayed as a refresh
 * token or vice versa.
 */
public class JwtTokenProvider {

    private static final String CLAIM_TYPE = "type";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_HOSPITAL_ID = "hospitalId";
    private static final String CLAIM_HOSPITAL_USER_ID = "hospitalUserId";
    private static final String CLAIM_STAFF_TYPE = "staffType";
    private static final String CLAIM_STAFF_TYPE_ID = "staffTypeId";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_LAB_IDS = "labIds";
    private static final String CLAIM_TOKEN_USE = "tokenUse";

    private final JwtProperties properties;
    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    public JwtTokenProvider(JwtProperties properties) {
        this.properties = properties;
        this.accessKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(properties.getRefreshSecret().getBytes(StandardCharsets.UTF_8));
    }

    public long getAccessExpirationSeconds() {
        return properties.getExpirationSeconds();
    }

    public String generateAccessToken(TokenClaims claims) {
        Instant now = Instant.now();
        var builder = Jwts.builder()
                .subject(claims.subject().toString())
                .issuer(properties.getIssuer())
                .claim(CLAIM_TYPE, claims.type().name())
                .claim(CLAIM_TOKEN_USE, "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(properties.getExpirationSeconds())));

        if (claims.email() != null) {
            builder.claim(CLAIM_EMAIL, claims.email());
        }
        if (claims.hospitalId() != null) {
            builder.claim(CLAIM_HOSPITAL_ID, claims.hospitalId().toString());
        }
        if (claims.hospitalUserId() != null) {
            builder.claim(CLAIM_HOSPITAL_USER_ID, claims.hospitalUserId().toString());
        }
        if (claims.staffType() != null) {
            builder.claim(CLAIM_STAFF_TYPE, claims.staffType());
        }
        if (claims.staffTypeId() != null) {
            builder.claim(CLAIM_STAFF_TYPE_ID, claims.staffTypeId().toString());
        }
        if (claims.roles() != null) {
            builder.claim(CLAIM_ROLES, claims.roles());
        }
        if (claims.labIds() != null && !claims.labIds().isEmpty()) {
            builder.claim(CLAIM_LAB_IDS, claims.labIds().stream().map(UUID::toString).toList());
        }

        return builder.signWith(accessKey).compact();
    }

    public String generateRefreshToken(UUID subject) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(subject.toString())
                .issuer(properties.getIssuer())
                .claim(CLAIM_TOKEN_USE, "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(properties.getRefreshExpirationSeconds())))
                .signWith(refreshKey)
                .compact();
    }

    /** Verifies and parses an access token. Throws {@link JwtException} if invalid, expired, or the wrong use. */
    public Claims parseAccessToken(String token) {
        Claims claims = parse(token, accessKey);
        requireTokenUse(claims, "access");
        return claims;
    }

    /** Verifies and parses a refresh token. Throws {@link JwtException} if invalid, expired, or the wrong use. */
    public Claims parseRefreshToken(String token) {
        Claims claims = parse(token, refreshKey);
        requireTokenUse(claims, "refresh");
        return claims;
    }

    public AuthPrincipal toPrincipal(Claims claims) {
        return new AuthPrincipal(
                UUID.fromString(claims.getSubject()),
                claims.get(CLAIM_EMAIL, String.class),
                UserType.valueOf(claims.get(CLAIM_TYPE, String.class)),
                uuidOrNull(claims.get(CLAIM_HOSPITAL_ID, String.class)),
                uuidOrNull(claims.get(CLAIM_HOSPITAL_USER_ID, String.class)),
                claims.get(CLAIM_STAFF_TYPE, String.class),
                uuidOrNull(claims.get(CLAIM_STAFF_TYPE_ID, String.class)),
                stringList(claims.get(CLAIM_ROLES, List.class)),
                uuidList(claims.get(CLAIM_LAB_IDS, List.class))
        );
    }

    private Claims parse(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void requireTokenUse(Claims claims, String expected) {
        Object use = claims.get(CLAIM_TOKEN_USE);
        if (!expected.equals(use)) {
            throw new JwtException("Unexpected token use: " + use);
        }
    }

    private static UUID uuidOrNull(String value) {
        return value == null ? null : UUID.fromString(value);
    }

    @SuppressWarnings("unchecked")
    private static List<String> stringList(List<?> raw) {
        if (raw == null) {
            return List.of();
        }
        return (List<String>) raw;
    }

    @SuppressWarnings("unchecked")
    private static List<UUID> uuidList(List<?> raw) {
        if (raw == null) {
            return List.of();
        }
        return ((List<String>) raw).stream().map(UUID::fromString).collect(Collectors.toList());
    }
}
