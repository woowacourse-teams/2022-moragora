package com.woowacourse.moragora.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthWebConfig implements WebMvcConfigurer {
    private static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    private final List<HandlerInterceptor> interceptors;
    private final List<HandlerMethodArgumentResolver> resolvers;
    private final String allowedOrigin;

    public AuthWebConfig(final List<HandlerInterceptor> interceptors,
                         final List<HandlerMethodArgumentResolver> resolvers,
                         @Value("${cors-config.allowed-origin}") final String allowedOrigin) {
        this.interceptors = interceptors;
        this.resolvers = resolvers;
        this.allowedOrigin = allowedOrigin;
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigin)
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.LOCATION);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        interceptors.forEach(interceptor -> registry.addInterceptor(interceptor)
                .excludePathPatterns("/docs/**")
                .addPathPatterns("/**"));
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(this.resolvers);
    }
}
