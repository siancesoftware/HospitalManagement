package com.siance.hm.security.web;

import com.siance.hm.common.response.ErrorResponse;
import com.siance.hm.common.util.RequestContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Gate for service-to-service "internal" endpoints (e.g. hm-auth-service's
 * user-creation/credential-verification API used only by other services,
 * never by end-user clients). Requests to a configured path pattern must
 * carry a matching {@code X-Internal-Api-Key} header.
 *
 * <p>This is a pragmatic stand-in for the mutual-TLS / service-mesh identity
 * a production deployment would use between internal services; swap it for
 * mTLS or a service-mesh policy (Istio, etc.) - see the implementation
 * plan's optional Istio service mesh - before going to production.
 */
public class InternalApiKeyFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "X-Internal-Api-Key";

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> protectedPatterns;
    private final String expectedKey;
    private final ObjectMapper objectMapper;

    public InternalApiKeyFilter(List<String> protectedPatterns, String expectedKey, ObjectMapper objectMapper) {
        this.protectedPatterns = protectedPatterns;
        this.expectedKey = expectedKey;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        boolean isProtected = protectedPatterns.stream().anyMatch(p -> pathMatcher.match(p, request.getRequestURI()));

        if (isProtected) {
            String provided = request.getHeader(HEADER_NAME);
            if (provided == null || !provided.equals(expectedKey)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(response.getWriter(), ErrorResponse.of("UNAUTHORIZED",
                        "Missing or invalid internal API key.", RequestContext.getRequestId()));
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
