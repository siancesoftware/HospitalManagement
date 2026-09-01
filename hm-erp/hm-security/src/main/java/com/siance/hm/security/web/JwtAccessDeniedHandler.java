package com.siance.hm.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siance.hm.common.response.ErrorResponse;
import com.siance.hm.common.util.RequestContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/** Emits the standard {@code {"error": {...}}} envelope for authenticated-but-forbidden (403) requests. */
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse body = ErrorResponse.of("FORBIDDEN", "You do not have permission to perform this action.",
                RequestContext.getRequestId());
        objectMapper.writeValue(response.getWriter(), body);
    }
}
