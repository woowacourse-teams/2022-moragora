package com.woowacourse.moragora.domain.auth;

import java.time.LocalDateTime;
import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refresh_token", timeToLive = 60000)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RefreshToken {

    @Id
    @Include
    private String uuid;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public RefreshToken(final String uuid, final Long userId, final LocalDateTime expiredAt) {
        this.uuid = uuid;
        this.userId = userId;
        this.expiredAt = expiredAt;
    }

    public boolean isExpiredAt(final LocalDateTime now) {
        return now.isAfter(expiredAt);
    }
}
