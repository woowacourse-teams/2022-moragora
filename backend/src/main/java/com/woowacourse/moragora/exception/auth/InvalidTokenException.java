package com.woowacourse.moragora.exception.auth;

import lombok.Getter;

@Getter
public class InvalidTokenException extends TokenException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";
    private static final String TOKEN_STATUS = "invalid";

    public InvalidTokenException() {
        super(MESSAGE, TOKEN_STATUS);
    }
}
