package com.woowacourse.moragora.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @DisplayName("Refresh token을 DB에 저장한다.")
    @Test
    void save() {
        // given
        final RefreshToken refreshToken = new RefreshToken("refresh_token", 1L, LocalDateTime.now());

        // when
        final RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        // then
        assertThat(savedRefreshToken).isNotNull();
    }

    @DisplayName("Refresh token을 DB에서 찾아온다.")
    @Test
    void findByValue() {
        // given
        final RefreshToken refreshToken = new RefreshToken("refresh_token", 1L, LocalDateTime.now());
        final RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        // when
        final Optional<RefreshToken> foundRefreshToken = refreshTokenRepository.findByValue(
                savedRefreshToken.getValue());

        // then
        assertThat(foundRefreshToken.get()).usingRecursiveComparison()
                .isEqualTo(savedRefreshToken);
    }

    @DisplayName("Refresh token을 DB에서 삭제한다.")
    @Test
    void deleteByValue() {
        // given
        final RefreshToken refreshToken = new RefreshToken("refresh_token", 1L, LocalDateTime.now());
        final RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        // when
        refreshTokenRepository.deleteByValue(savedRefreshToken.getValue());

        // then
        final Optional<RefreshToken> foundRefreshToken = refreshTokenRepository.findByValue(
                savedRefreshToken.getValue());
        assertThat(foundRefreshToken).isEmpty();
    }
}
