package com.siance.hm.security.service;

import java.util.Optional;

public interface CurrentUserService {

    Optional<String> getCurrentUserId();

    Optional<String> getCurrentUsername();

    Optional<String> getCurrentUserEmail();

    String getCurrentUserIdOrThrow();
}