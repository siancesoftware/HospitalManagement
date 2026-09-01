package com.siance.hm.common.exception;

/** Equivalent of NestJS's {@code ConflictException} (HTTP 409). */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
