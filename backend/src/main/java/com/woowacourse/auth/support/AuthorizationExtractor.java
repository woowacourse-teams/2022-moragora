package com.woowacourse.auth.support;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

public class AuthorizationExtractor {
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";
    public static final String BEARER_TYPE = "Bearer";

    public static String extract(final HttpServletRequest request) {
        final Enumeration<String> headers = extractHeaders(request);
        while (headers.hasMoreElements()) {
            final String value = headers.nextElement();
            if (isBearerToken(value)) {
                final String token = value.substring(BEARER_TYPE.length()).trim();
                request.setAttribute(ACCESS_TOKEN_TYPE, value.substring(0, BEARER_TYPE.length()).trim());
                return parseToken(token);
            }
        }
        return null;
    }

    private static Enumeration<String> extractHeaders(final HttpServletRequest request) {
        final Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        return headers;
    }

    private static boolean isBearerToken(final String value) {
        return value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase());
    }

    private static String parseToken(final String token) {
        final int commaIndex = token.indexOf(',');
        if (commaIndex > 0) {
            return token.substring(0, commaIndex);
        }
        return token;
    }
}
