package com.siance.hm.auth.dto;

import com.siance.hm.security.jwt.TokenPair;

/** Equivalent of the original {@code TokenResponseDto}: tokens + a profile snapshot. */
public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        UserProfileDto user
) {
    public static TokenResponse of(TokenPair tokens, UserProfileDto user) {
        return new TokenResponse(tokens.accessToken(), tokens.refreshToken(), tokens.tokenType(),
                tokens.expiresIn(), user);
    }
}
