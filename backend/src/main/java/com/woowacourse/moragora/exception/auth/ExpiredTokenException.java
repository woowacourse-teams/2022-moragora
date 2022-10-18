package com.woowacourse.moragora.exception.auth;

import lombok.Getter;

@Getter
public class ExpiredTokenException extends TokenException {

    private static final String MESSAGE = "만료된 토큰입니다.";
    private static final String TOKEN_STATUS = "expired";

    public ExpiredTokenException() {
        super(MESSAGE, TOKEN_STATUS);
    }
}

