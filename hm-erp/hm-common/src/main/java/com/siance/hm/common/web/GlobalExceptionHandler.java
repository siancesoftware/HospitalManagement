package com.siance.hm.common.web;

import com.siance.hm.common.exception.BadRequestException;
import com.siance.hm.common.exception.ConflictException;
import com.siance.hm.common.exception.ForbiddenException;
import com.siance.hm.common.exception.ResourceNotFoundException;
import com.siance.hm.common.exception.UnauthorizedException;
import com.siance.hm.common.response.ErrorResponse;
import com.siance.hm.common.util.RequestContext;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

/**
 * Global exception -&gt; HTTP response translator.
 *
 * <p>Port of the original {@code AllExceptionsFilter}: every error, whatever
 * its source, comes back as {@code { "error": { code, message, requestId,
 * validation? } } } with the same status/code mapping the NestJS filter used.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        warn(HttpStatus.NOT_FOUND, ex.getMessage());
        return respond(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        warn(HttpStatus.CONFLICT, ex.getMessage());
        return respond(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        warn(HttpStatus.BAD_REQUEST, ex.getMessage());
        return respond(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        log.debug("[{}] 401 {}", RequestContext.getRequestId(), ex.getMessage());
        return respond(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
        log.debug("[{}] 403 {}", RequestContext.getRequestId(), ex.getMessage());
        return respond(HttpStatus.FORBIDDEN, "FORBIDDEN", ex.getMessage());
    }

    // ── Spring Security ─────────────────────────────────────────────────────

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {
        log.debug("[{}] 401 {}", RequestContext.getRequestId(), ex.getMessage());
        return respond(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Invalid credentials or inactive account.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return respond(HttpStatus.FORBIDDEN, "FORBIDDEN", "You do not have permission to perform this action.");
    }

    // ── Bean Validation (equivalent of class-validator ValidationPipe) ─────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorResponse.FieldValidationError> validation = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldValidationError(
                        fe.getField(), "INVALID", fe.getDefaultMessage()))
                .toList();
        ErrorResponse body = ErrorResponse.of("VALIDATION_FAILED", "One or more fields are invalid.",
                RequestContext.getRequestId(), validation);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorResponse.FieldValidationError> validation = ex.getConstraintViolations().stream()
                .map(cv -> new ErrorResponse.FieldValidationError(
                        cv.getPropertyPath().toString(), "INVALID", cv.getMessage()))
                .toList();
        ErrorResponse body = ErrorResponse.of("VALIDATION_FAILED", "One or more fields are invalid.",
                RequestContext.getRequestId(), validation);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleMalformedRequest(Exception ex) {
        return respond(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "The request could not be parsed: " + ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return respond(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", ex.getMessage());
    }

    // ── Database (equivalent of Prisma P2002 / P2025 handling) ─────────────

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("[{}] Data integrity violation: {}", RequestContext.getRequestId(), ex.getMessage());
        return respond(HttpStatus.CONFLICT, "CONFLICT", "A record with these details already exists.");
    }

    // ── Fallback ─────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("[{}] Unhandled exception", RequestContext.getRequestId(), ex);
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected error occurred.");
    }

    private void warn(HttpStatus status, String message) {
        log.warn("[{}] {} {}", RequestContext.getRequestId(), status.value(), message);
    }

    private ResponseEntity<ErrorResponse> respond(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(ErrorResponse.of(code, message, RequestContext.getRequestId()));
    }
}
