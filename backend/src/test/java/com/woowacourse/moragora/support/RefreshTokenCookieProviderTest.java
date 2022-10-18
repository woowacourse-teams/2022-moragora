package com.woowacourse.moragora.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.exception.auth.EmptyTokenException;
import com.woowacourse.moragora.presentation.auth.RefreshTokenCookieProvider;
import java.time.Duration;
import javax.servlet.http.Cookie;
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
                .path("/token")
                .build();

        // when
        final ResponseCookie actual = refreshTokenCookieProvider.create(refreshToken);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("쿠키를 제거하기 위해 maxAge가 0인 빈 쿠키를 생성한다.")
    @Test
    void createInvalidCookie() {
        // given
        final RefreshTokenCookieProvider refreshTokenCookieProvider = new RefreshTokenCookieProvider(0);

        final ResponseCookie expected = ResponseCookie.from("refreshToken", "")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite(SameSite.NONE.attributeValue())
                .path("/token")
                .build();

        // when
        final ResponseCookie actual = refreshTokenCookieProvider.createInvalidCookie();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("쿠키에서 refreshToken을 꺼내온다.")
    @Test
    void extractRefreshToken() {
        // given
        final int validityInMilliseconds = 1000;
        final RefreshTokenCookieProvider refreshTokenCookieProvider = new RefreshTokenCookieProvider(
                validityInMilliseconds);
        final Cookie cookie = new Cookie("refreshToken", "fake_token");

        // when
        final String cookieValue = refreshTokenCookieProvider.extractRefreshToken(new Cookie[]{cookie});

        // then
        assertThat(cookieValue).isEqualTo("fake_token");
    }

    @DisplayName("쿠키가 null인 경우 예외가 발생한다.")
    @Test
    void extractRefreshToken_throwsException_ifCookiesNull() {
        // given
        final RefreshTokenCookieProvider refreshTokenCookieProvider = new RefreshTokenCookieProvider(1000);

        // when, then
        assertThatThrownBy(() -> refreshTokenCookieProvider.extractRefreshToken(null))
                .isInstanceOf(EmptyTokenException.class);
    }

    @DisplayName("쿠키에 refresh token이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void extractRefreshToken_throwsException_ifRefreshTokenNotInCookie() {
        // given
        final RefreshTokenCookieProvider refreshTokenCookieProvider = new RefreshTokenCookieProvider(1000);
        final Cookie cookie = new Cookie("token", "false");

        // when, then
        assertThatThrownBy(() -> refreshTokenCookieProvider.extractRefreshToken(new Cookie[]{cookie}))
                .isInstanceOf(EmptyTokenException.class);
    }
}
