package com.woowacourse.moragora.exception.auth;

import lombok.Getter;

@Getter
public class EmptyTokenException extends TokenException {

    private static final String MESSAGE = "토큰이 존재하지 않습니다.";
    private static final String TOKEN_STATUS = "empty";

    public EmptyTokenException() {
        super(MESSAGE, TOKEN_STATUS);
    }
}
