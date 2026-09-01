package com.siance.hm.common.util;

import java.util.UUID;

/**
 * Holds the per-request correlation id (equivalent of the original
 * {@code X-Request-Id} handling in {@code TransformInterceptor} /
 * {@code AllExceptionsFilter}) so any layer can attach it to a response
 * envelope without threading it through every method signature.
 *
 * <p>Populated by {@link RequestIdFilter} at the very start of the filter
 * chain and cleared once the request completes.
 */
public final class RequestContext {

    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();

    private RequestContext() {
    }

    public static void setRequestId(String requestId) {
        REQUEST_ID.set(requestId);
    }

    public static String getRequestId() {
        String id = REQUEST_ID.get();
        return id != null ? id : newRequestId();
    }

    public static String newRequestId() {
        return "req_" + UUID.randomUUID();
    }

    public static void clear() {
        REQUEST_ID.remove();
    }
}
