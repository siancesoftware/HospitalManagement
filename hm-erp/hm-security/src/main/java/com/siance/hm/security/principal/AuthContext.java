package com.siance.hm.security.principal;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Static accessor for the current request's {@link AuthPrincipal}, equivalent
 * to reading {@code request.user} anywhere in the original codebase without
 * threading it through every method signature.
 */
public final class AuthContext {

    private AuthContext() {
    }

    public static Optional<AuthPrincipal> current() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthPrincipal principal)) {
            return Optional.empty();
        }
        return Optional.of(principal);
    }

    /** Throws if there is no authenticated principal - use only where a filter chain already guarantees one. */
    public static AuthPrincipal require() {
        return current().orElseThrow(() ->
                new IllegalStateException("No authenticated principal in the current security context"));
    }
}
