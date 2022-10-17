package com.woowacourse.moragora.application.auth;

import com.woowacourse.moragora.domain.auth.RefreshToken;
import com.woowacourse.moragora.domain.auth.RefreshTokenRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class RefreshTokenProvider {

    private final long validityInMilliseconds;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenProvider(@Value("${security.refresh.token.expire-length}") final long validityInMilliseconds,
                                final RefreshTokenRepository refreshTokenRepository) {
        this.validityInMilliseconds = validityInMilliseconds;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public String create(final Long userId, final LocalDateTime now) {
        final String value = UUID.randomUUID().toString();
        final LocalDateTime expiredAt = now.plus(validityInMilliseconds, ChronoUnit.MILLIS);
        final RefreshToken refreshToken = new RefreshToken(value, userId, expiredAt);
        final RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        return savedRefreshToken.getValue();
    }

    public Optional<RefreshToken> findRefreshToken(final String key) {
        return refreshTokenRepository.findByValue(key);
    }

    @Transactional
    public void remove(final String oldToken) {
        refreshTokenRepository.deleteByValue(oldToken);
    }
}
