package com.siance.hm.security.web;

import com.siance.hm.common.exception.UnauthorizedException;
import com.siance.hm.security.principal.AuthContext;
import com.siance.hm.security.principal.AuthPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/** Resolves {@code @CurrentUser AuthPrincipal principal} controller parameters. */
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && AuthPrincipal.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return AuthContext.current()
                .orElseThrow(() -> new UnauthorizedException("Authentication required."));
    }
}
