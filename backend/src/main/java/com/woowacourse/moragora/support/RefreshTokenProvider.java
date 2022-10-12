package com.woowacourse.moragora.support;

import com.woowacourse.moragora.application.auth.RefreshToken;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenProvider {

    private static final Map<String, RefreshToken> values = new HashMap<>();

    private final long validityInMilliseconds;

    public RefreshTokenProvider(@Value("${security.refresh.token.expire-length}") final long validityInMilliseconds) {
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String create(final Long userId, final LocalDateTime now) {
        final String value = UUID.randomUUID().toString();
        final LocalDateTime expiredAt = now.plus(validityInMilliseconds, ChronoUnit.MILLIS);
        final RefreshToken refreshToken = new RefreshToken(userId, value, expiredAt);
        values.put(value, refreshToken);

        return value;
    }

    public Map<String, RefreshToken> getValues() {
        return values;
    }
}
