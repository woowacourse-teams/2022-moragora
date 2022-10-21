package com.woowacourse.moragora.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RefreshTokenTest {

    @DisplayName("만료된 토큰인지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"2022-10-15T12:00,2022-10-15T13:00,false", "2022-10-15T12:00,2022-10-15T11:00,true"})
    void isExpired(final LocalDateTime now, final LocalDateTime expiredAt, final boolean expected) {
        // given
        final RefreshToken refreshToken = new RefreshToken("refresh_token", 1L, expiredAt);

        // when
        final boolean isExpired = refreshToken.isExpiredAt(now);

        // then
        assertThat(isExpired).isEqualTo(expected);
    }
}
