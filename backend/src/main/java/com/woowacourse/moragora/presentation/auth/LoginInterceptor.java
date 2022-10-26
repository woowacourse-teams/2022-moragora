package com.woowacourse.moragora.presentation.auth;

import com.woowacourse.moragora.support.AuthorizationExtractor;
import com.woowacourse.moragora.support.JwtTokenProvider;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        if (request.getMethod().equals((HttpMethod.OPTIONS.name()))) {
            return true;
        }

        if (isNotAnnotated(handler)) {
            return true;
        }

        if (!isValidToken(request)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    private boolean isNotAnnotated(final Object handler) {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Authentication classAnnotation = handlerMethod.getBeanType().getAnnotation(Authentication.class);
        final Authentication methodAnnotation = handlerMethod.getMethodAnnotation(Authentication.class);
        return Objects.isNull(classAnnotation) && Objects.isNull(methodAnnotation);
    }

    private boolean isValidToken(final HttpServletRequest request) {
        final String token = AuthorizationExtractor.extract(request);
        return jwtTokenProvider.validateToken(token);
    }
}
