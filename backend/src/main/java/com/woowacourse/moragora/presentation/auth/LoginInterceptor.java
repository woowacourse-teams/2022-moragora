package com.woowacourse.moragora.presentation.auth;

import com.woowacourse.moragora.application.auth.JwtTokenProvider;
import com.woowacourse.moragora.support.AuthorizationExtractor;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
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
        if (isNotOptionsMethod(request) && isAnnotated(handler)) {
            validateToken(request);
        }

        return true;
    }

    private boolean isNotOptionsMethod(final HttpServletRequest request) {
        return HttpMethod.valueOf(request.getMethod()) != HttpMethod.OPTIONS;
    }

    private boolean isAnnotated(final Object handler) {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Authentication classAnnotation = handlerMethod.getBeanType().getAnnotation(Authentication.class);
        final Authentication methodAnnotation = handlerMethod.getMethodAnnotation(Authentication.class);
        return Objects.nonNull(classAnnotation) || Objects.nonNull(methodAnnotation);
    }

    private void validateToken(final HttpServletRequest request) {
        final String token = AuthorizationExtractor.extract(request);
        jwtTokenProvider.validateToken(token);
    }
}
