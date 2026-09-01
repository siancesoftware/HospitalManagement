package com.siance.hm.auth.controller;

import com.siance.hm.auth.dto.CreateUserRequest;
import com.siance.hm.auth.dto.UserSummary;
import com.siance.hm.auth.dto.VerifyCredentialsRequest;
import com.siance.hm.auth.service.AuthService;
import com.siance.hm.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service-to-service only (see {@code InternalApiKeyFilter} wiring in
 * {@code SecurityConfig}) - never exposed to end-user clients, and not
 * routed through hm-gateway. Lets hm-hospital-service (and, in future,
 * other services that own a principal type) create/verify the shared
 * credential record without ever touching the {@code users} table
 * directly, preserving the database-per-service rule.
 */
@Hidden
@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserSummary> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.of(authService.createUser(request));
    }

    @PostMapping("/verify-credentials")
    public ApiResponse<UserSummary> verifyCredentials(@Valid @RequestBody VerifyCredentialsRequest request) {
        return ApiResponse.of(authService.verifyCredentials(request));
    }
}
