package com.siance.hm.auth.service;

import com.siance.hm.auth.dto.CreateUserRequest;
import com.siance.hm.auth.dto.LoginRequest;
import com.siance.hm.auth.dto.RegisterRequest;
import com.siance.hm.auth.dto.TokenResponse;
import com.siance.hm.auth.dto.UserProfileDto;
import com.siance.hm.auth.dto.UserSummary;
import com.siance.hm.auth.dto.VerifyCredentialsRequest;
import com.siance.hm.auth.entity.Role;
import com.siance.hm.auth.entity.User;
import com.siance.hm.auth.entity.UserRole;
import com.siance.hm.auth.entity.UserStatus;
import com.siance.hm.auth.repository.RoleRepository;
import com.siance.hm.auth.repository.UserRepository;
import com.siance.hm.auth.repository.UserRoleRepository;
import com.siance.hm.common.exception.ConflictException;
import com.siance.hm.common.exception.ResourceNotFoundException;
import com.siance.hm.common.exception.UnauthorizedException;
import com.siance.hm.security.jwt.JwtTokenProvider;
import com.siance.hm.security.jwt.TokenClaims;
import com.siance.hm.security.jwt.TokenPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);
    private static final String DEFAULT_ROLE_CODE = "USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public TokenResponse register(RegisterRequest request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ConflictException(
                    "Email already in use: " + request.getEmail()
            );
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );
        user.setPasswordChangedAt(OffsetDateTime.now());

        user = userRepository.save(user);

        assignRole(user, DEFAULT_ROLE_CODE);

        return issueTokens(user);
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid credentials.")
                );

        assertUsable(user);

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )) {
            registerFailedAttempt(user);
            userRepository.save(user);

            throw new UnauthorizedException("Invalid credentials.");
        }

        user.setFailedAttempts(0);
        user.setLockedUntil(null);
        user.setLastLoginAt(OffsetDateTime.now());

        return issueTokens(user);
    }

    @Override
    @Transactional
    public TokenResponse refreshTokens(String refreshToken) {

        Claims claims;

        try {
            claims = jwtTokenProvider.parseRefreshToken(refreshToken);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new UnauthorizedException(
                    "Invalid or expired refresh token."
            );
        }

        User user = userRepository
                .findById(UUID.fromString(claims.getSubject()))
                .orElseThrow(() ->
                        new UnauthorizedException(
                                "Invalid refresh token."
                        )
                );

        if (user.getRefreshTokenHash() == null
                || !passwordEncoder.matches(
                        refreshToken,
                        user.getRefreshTokenHash()
                )) {

            throw new UnauthorizedException(
                    "Refresh token has been revoked."
            );
        }

        assertUsable(user);

        return issueTokens(user);
    }

    @Override
    @Transactional
    public void logout(UUID userId) {

        userRepository.findById(userId).ifPresent(user -> {
            user.setRefreshTokenHash(null);
            userRepository.save(user);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getProfile(UUID userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        ResourceNotFoundException.of("User", userId)
                );

        return toProfile(user);
    }

    // -------------------------------------------------------------------------
    // Internal / service-to-service
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public UserSummary createUser(CreateUserRequest request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ConflictException(
                    "Email already in use: " + request.getEmail()
            );
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );
        user.setPasswordChangedAt(OffsetDateTime.now());

        user = userRepository.save(user);

        List<String> roleCodes =
                (request.getRoleCodes() == null
                        || request.getRoleCodes().isEmpty())
                        ? List.of(DEFAULT_ROLE_CODE)
                        : request.getRoleCodes();

        for (String code : roleCodes) {
            assignRole(user, code);
        }

        return toSummary(user);
    }

    @Override
    @Transactional
    public UserSummary verifyCredentials(
            VerifyCredentialsRequest request
    ) {

        User user = userRepository
                .findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() ->
                        new UnauthorizedException(
                                "Invalid credentials."
                        )
                );

        assertUsable(user);

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )) {
            registerFailedAttempt(user);
            userRepository.save(user);

            throw new UnauthorizedException(
                    "Invalid credentials."
            );
        }

        user.setFailedAttempts(0);
        user.setLockedUntil(null);

        userRepository.save(user);

        return toSummary(user);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void assertUsable(User user) {

        if (user.isLocked()) {
            throw new UnauthorizedException(
                    "Account is temporarily locked. Try again later."
            );
        }

        if (!user.isActive()
                || user.getStatus() != UserStatus.ACTIVE) {

            throw new UnauthorizedException(
                    "Account is not active."
            );
        }
    }

    private void registerFailedAttempt(User user) {

        user.setFailedAttempts(
                user.getFailedAttempts() + 1
        );

        if (user.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
            user.setLockedUntil(
                    OffsetDateTime.now().plus(LOCK_DURATION)
            );
        }
    }

    private void assignRole(User user, String roleCode) {

        Role role = roleRepository
                .findByCode(roleCode)
                .orElseGet(() ->
                        roleRepository.save(
                                new Role(
                                        roleCode,
                                        roleCode,
                                        true
                                )
                        )
                );

        userRoleRepository.save(
                new UserRole(user, role)
        );
    }

    private TokenResponse issueTokens(User user) {

        List<String> roles = roleCodesOf(user);

        TokenClaims claims =
                TokenClaims.platformAdmin(
                        user.getId(),
                        user.getEmail(),
                        roles
                );

        String accessToken =
                jwtTokenProvider.generateAccessToken(claims);

        String refreshToken =
                jwtTokenProvider.generateRefreshToken(
                        user.getId()
                );

        user.setRefreshTokenHash(
                passwordEncoder.encode(refreshToken)
        );

        userRepository.save(user);

        TokenPair pair = TokenPair.bearer(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessExpirationSeconds()
        );

        return TokenResponse.of(
                pair,
                toProfile(user, roles)
        );
    }

    private List<String> roleCodesOf(User user) {

        return userRoleRepository
                .findByUserId(user.getId())
                .stream()
                .map(ur -> ur.getRole().getCode())
                .distinct()
                .toList();
    }

    private UserProfileDto toProfile(User user) {
        return toProfile(
                user,
                roleCodesOf(user)
        );
    }

    private UserProfileDto toProfile(
            User user,
            List<String> roles
    ) {

        return new UserProfileDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                roles,
                user.getStatus().name(),
                user.isMfaEnabled(),
                user.getLastLoginAt()
        );
    }

    private UserSummary toSummary(User user) {

        return new UserSummary(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isActive(),
                roleCodesOf(user)
        );
    }
}