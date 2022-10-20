package com.woowacourse.moragora.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @DisplayName("Refresh token을 DB에 저장한다.")
    @Test
    void save() {
        // given
        final RefreshToken refreshToken = new RefreshToken("uuid_1", 1L, LocalDateTime.now());

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
        final Optional<RefreshToken> foundRefreshToken = refreshTokenRepository.findById(
                savedRefreshToken.getUuid());

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
        refreshTokenRepository.deleteById(savedRefreshToken.getUuid());

        // then
        final Optional<RefreshToken> foundRefreshToken = refreshTokenRepository.findById(
                savedRefreshToken.getUuid());
        assertThat(foundRefreshToken).isEmpty();
    }
}
