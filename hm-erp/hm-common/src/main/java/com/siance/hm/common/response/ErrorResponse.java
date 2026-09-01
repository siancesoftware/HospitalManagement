package com.siance.hm.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

/**
 * Error envelope. Mirrors the original {@code AllExceptionsFilter} shape:
 * {@code { "error": { "code", "message", "requestId", "details"?, "validation"? } } }
 */
public record ErrorResponse(ErrorBody error) {

    public static ErrorResponse of(String code, String message, String requestId) {
        return new ErrorResponse(new ErrorBody(code, message, requestId, null, null));
    }

    public static ErrorResponse of(String code, String message, String requestId,
                                    List<FieldValidationError> validation) {
        return new ErrorResponse(new ErrorBody(code, message, requestId, null, validation));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorBody(
            String code,
            String message,
            String requestId,
            Map<String, Object> details,
            List<FieldValidationError> validation
    ) {
    }

    public record FieldValidationError(String field, String code, String message) {
    }
}
