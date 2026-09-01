package com.siance.hm.security.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injects the authenticated {@code AuthPrincipal} into a controller method
 * parameter - equivalent of the original {@code @CurrentUser()} decorator.
 *
 * <pre>{@code
 * @GetMapping("/me")
 * public ApiResponse<ProfileDto> me(@CurrentUser AuthPrincipal principal) { ... }
 * }</pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
