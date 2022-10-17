package com.woowacourse.moragora.presentation.auth;

import com.woowacourse.moragora.exception.InvalidTokenException;
import java.time.Duration;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieProvider {

    private static final String REFRESH_TOKEN = "refreshToken";

    private final long validityInMilliseconds;

    public RefreshTokenCookieProvider(
            @Value("${security.refresh.token.expire-length}") final long validityInMilliseconds) {
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public ResponseCookie create(final String refreshToken) {
        return createTokenCookieBuilder(refreshToken)
                .maxAge(Duration.ofMillis(validityInMilliseconds))
                .build();
    }

    public ResponseCookie createInvalidCookie() {
        return createTokenCookieBuilder("")
                .maxAge(0)
                .build();
    }

    public String extractRefreshToken(final Cookie[] cookies) {
        final Cookie cookie = Arrays.stream(cookies)
                .filter(it -> it.getName().equals(REFRESH_TOKEN))
                .findAny()
                .orElseThrow(InvalidTokenException::new);

        return cookie.getValue();
    }

    private ResponseCookieBuilder createTokenCookieBuilder(final String value) {
        return ResponseCookie.from(REFRESH_TOKEN, value)
                .httpOnly(true)
                .secure(true)
                .path("/token")
                .sameSite(SameSite.NONE.attributeValue());
    }
}
