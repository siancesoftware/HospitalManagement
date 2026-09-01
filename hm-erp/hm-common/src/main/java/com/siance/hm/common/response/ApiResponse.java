package com.siance.hm.common.response;

import com.siance.hm.common.util.RequestContext;

/**
 * Standard single-resource success envelope.
 *
 * Mirrors the original NestJS {@code TransformInterceptor} contract:
 * {@code { "data": ..., "requestId": "..." } }.
 */
public record ApiResponse<T>(T data, String requestId) {

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(data, RequestContext.getRequestId());
    }

    public static <T> ApiResponse<T> of(T data, String requestId) {
        return new ApiResponse<>(data, requestId);
    }
}
