package com.woowacourse.auth.config;

import com.woowacourse.auth.exception.UnauthorizedTokenException;
import com.woowacourse.auth.support.AuthorizationExtractor;
import com.woowacourse.auth.support.JwtTokenProvider;
import com.woowacourse.auth.support.Login;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
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

        if (!hasAnnotation(handler)) {
            return true;
        }

        validateToken(request);
        return true;
    }

    private boolean hasAnnotation(final Object handler) {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;

        return null != handlerMethod.getMethodAnnotation(Login.class);
    }

    private void validateToken(final HttpServletRequest request) {
        final String token = AuthorizationExtractor.extract(request);
        final boolean isValidate = jwtTokenProvider.validateToken(token);
        if (!isValidate) {
            throw new UnauthorizedTokenException();
        }
    }
}
