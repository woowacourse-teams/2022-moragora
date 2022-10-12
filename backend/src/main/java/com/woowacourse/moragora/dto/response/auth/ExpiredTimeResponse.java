package com.woowacourse.moragora.dto.response.auth;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExpiredTimeResponse {

    private final long expiredTime;

    public ExpiredTimeResponse(final LocalDateTime expiredTime) {
        this.expiredTime = Timestamp.valueOf(expiredTime).getTime();
    }
}
