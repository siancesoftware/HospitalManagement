package com.siance.hm.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class CurrentUserService {

    public Optional<String> getCurrentUserId() {
        return getJwt().map(jwt -> jwt.getClaimAsString("sub"));
    }

    public Optional<String> getCurrentUsername() {
        return getJwt().map(jwt -> jwt.getClaimAsString("preferred_username"));
    }

    public Optional<String> getCurrentUserEmail() {
        return getJwt().map(jwt -> jwt.getClaimAsString("email"));
    }

    public String getCurrentUserIdOrThrow() {
        return getCurrentUserId().orElseThrow(() -> new RuntimeException("No authenticated user"));
    }

    private Optional<Jwt> getJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return Optional.of(jwt);
        }
        return Optional.empty();
    }
}
