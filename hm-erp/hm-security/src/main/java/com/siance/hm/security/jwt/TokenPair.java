package com.siance.hm.security.jwt;

/** Equivalent of the original {@code AuthTokensDto}. */
public record TokenPair(String accessToken, String refreshToken, String tokenType, long expiresIn) {

    public static TokenPair bearer(String accessToken, String refreshToken, long expiresInSeconds) {
        return new TokenPair(accessToken, refreshToken, "Bearer", expiresInSeconds);
    }
}
