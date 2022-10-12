package com.woowacourse.moragora.support;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.presentation.auth.RefreshTokenCookieProvider;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;

class RefreshTokenCookieProviderTest {

    @DisplayName("Refresh token을 받아 Response cookie를 생성한다.")
    @Test
    void create() {
        // given
        final String refreshToken = "refresh_token";
        final int validityInMilliseconds = 1000;
        final RefreshTokenCookieProvider refreshTokenCookieProvider = new RefreshTokenCookieProvider(
                validityInMilliseconds);

        final ResponseCookie expected = ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(Duration.ofMillis(validityInMilliseconds))
                .httpOnly(true)
                .secure(true)
                .sameSite(SameSite.NONE.attributeValue())
                .path("/")
                .build();

        // when
        final ResponseCookie actual = refreshTokenCookieProvider.create(refreshToken);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
