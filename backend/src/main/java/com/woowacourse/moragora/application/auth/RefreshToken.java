package com.woowacourse.moragora.application.auth;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class RefreshToken {

    private final Long userId;
    private final String value;
    private final LocalDateTime expiredAt;

    public RefreshToken(final Long userId, final String value, final LocalDateTime expiredAt) {
        this.userId = userId;
        this.value = value;
        this.expiredAt = expiredAt;
    }

    public boolean isExpired(final LocalDateTime now) {
        return now.isAfter(expiredAt);
    }
}
