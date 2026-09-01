package com.siance.hm.common.exception;

/**
 * Equivalent of NestJS's {@code UnauthorizedException} (HTTP 401).
 * Thrown for invalid credentials, inactive accounts, invalid/expired
 * refresh tokens, etc. - the same cases the original AuthService and
 * JwtStrategy guarded against.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
