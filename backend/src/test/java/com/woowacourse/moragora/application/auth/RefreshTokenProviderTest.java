package com.woowacourse.moragora.application.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.domain.auth.RefreshToken;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RefreshTokenProviderTest {

    @Autowired
    private RefreshTokenProvider refreshTokenProvider;

    @Value("${security.refresh.token.expire-length}")
    private long validityInMilliseconds;

    @DisplayName("Refresh token을 생성하고 저장한다.")
    @Test
    void create() {
        // given
        final LocalDateTime now = LocalDateTime.now();

        // when
        final String key = refreshTokenProvider.create(1L, now);

        // then
        final RefreshToken expected = new RefreshToken(key, 1L, now.plus(validityInMilliseconds, ChronoUnit.MILLIS));
        final Optional<RefreshToken> foundRefreshToken = refreshTokenProvider.findRefreshToken(key);
        assertThat(foundRefreshToken.get()).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("Refresh token을 삭제한다. ")
    @Test
    void remove() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final String key = refreshTokenProvider.create(1L, now);

        // when
        refreshTokenProvider.remove(key);

        // then
        assertThat(refreshTokenProvider.findRefreshToken(key)).isEmpty();
    }
}
