package com.siance.hm.common.exception;

/** Equivalent of NestJS's {@code BadRequestException} (HTTP 400). */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
