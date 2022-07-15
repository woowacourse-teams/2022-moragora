package com.woowacourse.auth.config;

import com.woowacourse.auth.support.AuthorizationExtractor;
import com.woowacourse.auth.support.JwtTokenProvider;
import com.woowacourse.auth.support.Authentication;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        if (request.getMethod().equals((HttpMethod.OPTIONS.name()))) {
            return true;
        }

        if (isNotAnnotated(handler)) {
            return true;
        }

        if (!isValidateToken(request)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    private boolean isNotAnnotated(final Object handler) {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Authentication login = handlerMethod.getMethodAnnotation(Authentication.class);
        return Objects.isNull(login);
    }

    private boolean isValidateToken(final HttpServletRequest request) {
        final String token = AuthorizationExtractor.extract(request);
        return jwtTokenProvider.validateToken(token);
    }
}
