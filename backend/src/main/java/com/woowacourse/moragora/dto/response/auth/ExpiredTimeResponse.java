package com.woowacourse.moragora.dto.response.auth;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExpiredTimeResponse {

    private final long expiredTime;

    public ExpiredTimeResponse(final long expiredTime) {
        this.expiredTime = expiredTime;
    }
}
