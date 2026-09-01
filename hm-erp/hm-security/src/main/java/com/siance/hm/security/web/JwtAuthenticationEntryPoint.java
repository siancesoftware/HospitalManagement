package com.siance.hm.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siance.hm.common.response.ErrorResponse;
import com.siance.hm.common.util.RequestContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/** Emits the standard {@code {"error": {...}}} envelope for unauthenticated (401) requests. */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse body = ErrorResponse.of("UNAUTHORIZED", "Authentication is required to access this resource.",
                RequestContext.getRequestId());
        objectMapper.writeValue(response.getWriter(), body);
    }
}
