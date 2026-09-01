package com.siance.hm.common.exception;

/** Equivalent of NestJS's {@code ForbiddenException} (HTTP 403) - authenticated but not permitted. */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
