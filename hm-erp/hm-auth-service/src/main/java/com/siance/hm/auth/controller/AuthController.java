package com.siance.hm.auth.controller;

import com.siance.hm.auth.dto.LoginRequest;
import com.siance.hm.auth.dto.RefreshRequest;
import com.siance.hm.auth.dto.RegisterRequest;
import com.siance.hm.auth.dto.TokenResponse;
import com.siance.hm.auth.dto.UserProfileDto;
import com.siance.hm.auth.service.AuthService;
import com.siance.hm.common.response.ApiResponse;
import com.siance.hm.security.principal.AuthPrincipal;
import com.siance.hm.security.web.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Platform-level authentication (super-admin / plain platform users).
 * Port of the original {@code AuthController}: POST /auth/register,
 * POST /auth/login, POST /auth/refresh, POST /auth/logout, GET /auth/me.
 */
@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new platform user",
            description = "Creates a new USER-role account. Returns an access + refresh token pair on success.")
    public ApiResponse<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.of(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ApiResponse<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.of(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Exchange a refresh token for a new token pair")
    public ApiResponse<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ApiResponse.of(authService.refreshTokens(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "Revoke the current refresh token")
    public ApiResponse<Void> logout(@CurrentUser AuthPrincipal principal) {
        authService.logout(principal.id());
        return ApiResponse.of(null);
    }

    @GetMapping("/me")
    @Operation(summary = "Get the current authenticated user's profile")
    public ApiResponse<UserProfileDto> me(@CurrentUser AuthPrincipal principal) {
        return ApiResponse.of(authService.getProfile(principal.id()));
    }
}
