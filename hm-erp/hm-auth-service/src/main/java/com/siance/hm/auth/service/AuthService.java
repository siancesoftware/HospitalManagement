package com.siance.hm.auth.service;

import com.siance.hm.auth.dto.CreateUserRequest;
import com.siance.hm.auth.dto.LoginRequest;
import com.siance.hm.auth.dto.RegisterRequest;
import com.siance.hm.auth.dto.TokenResponse;
import com.siance.hm.auth.dto.UserProfileDto;
import com.siance.hm.auth.dto.UserSummary;
import com.siance.hm.auth.dto.VerifyCredentialsRequest;

import java.util.UUID;

public interface AuthService {

    TokenResponse register(RegisterRequest request);

    TokenResponse login(LoginRequest request);

    TokenResponse refreshTokens(String refreshToken);

    void logout(UUID userId);

    UserProfileDto getProfile(UUID userId);

    UserSummary createUser(CreateUserRequest request);

    UserSummary verifyCredentials(VerifyCredentialsRequest request);
}