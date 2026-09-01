package com.siance.hm.common.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Reads the inbound {@code X-Request-Id} header, or mints a new one, stores
 * it for the duration of the request (for {@link ApiResponse}/{@code ErrorResponse}
 * envelopes and log correlation via MDC), and echoes it back on the response.
 */
public class RequestIdFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "X-Request-Id";
    private static final String MDC_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String requestId = request.getHeader(HEADER_NAME);
        if (!StringUtils.hasText(requestId)) {
            requestId = RequestContext.newRequestId();
        }

        RequestContext.setRequestId(requestId);
        MDC.put(MDC_KEY, requestId);
        response.setHeader(HEADER_NAME, requestId);

        try {
            chain.doFilter(request, response);
        } finally {
            RequestContext.clear();
            MDC.remove(MDC_KEY);
        }
    }
}
