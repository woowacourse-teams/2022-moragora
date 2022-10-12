package com.woowacourse.moragora.support;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.application.auth.RefreshToken;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RefreshTokenProviderTest {

    @DisplayName("Refresh token을 생성하고 in-memory map에 저장한다.")
    @Test
    void create() {
        // given
        final long validityInMilliseconds = 1000;
        final RefreshTokenProvider refreshTokenProvider = new RefreshTokenProvider(validityInMilliseconds);
        final LocalDateTime now = LocalDateTime.now();

        // when
        final String key = refreshTokenProvider.create(1L, now);

        // then
        final RefreshToken expected = new RefreshToken(1L, key, now.plus(validityInMilliseconds, ChronoUnit.MILLIS));
        assertThat(refreshTokenProvider.getValues()).containsEntry(key, expected);
    }
}
