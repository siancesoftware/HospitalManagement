package com.siance.hm.security.web;

import com.siance.hm.security.jwt.JwtTokenProvider;
import com.siance.hm.security.principal.AuthPrincipal;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Reads {@code Authorization: Bearer <token>}, verifies it, and - on success -
 * populates the Spring Security context with an {@link AuthPrincipal} plus
 * {@code ROLE_*} authorities derived from the token's {@code roles} claim.
 *
 * <p>Equivalent of the original global {@code JwtAuthGuard} + {@code JwtStrategy}
 * pairing. Routes are opened up per-service via each service's
 * {@code SecurityConfig} (path-based {@code permitAll()}), mirroring the
 * original's {@code @Public()} decorator.
 *
 * <p>Invalid/expired/missing tokens simply leave the context unauthenticated;
 * Spring Security's {@code authorizeHttpRequests} rules then reject the
 * request with 401/403, handled by {@link JwtAuthenticationEntryPoint} /
 * {@link JwtAccessDeniedHandler} to keep the same error envelope as
 * everything else.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            String token = header.substring(BEARER_PREFIX.length());
            try {
                var claims = tokenProvider.parseAccessToken(token);
                AuthPrincipal principal = tokenProvider.toPrincipal(claims);

                List<GrantedAuthority> authorities = principal.roles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .map(GrantedAuthority.class::cast)
                        .toList();

                var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException ex) {
                log.debug("Rejected access token: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }
}
