package com.woowacourse.moragora.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExpiredTokenException extends ClientRuntimeException {

    private static final String MESSAGE = "만료된 토큰입니다.";
    private static final String TOKEN_STATUS = "expired";

    private final String status;

    public ExpiredTokenException() {
        super(MESSAGE, HttpStatus.UNAUTHORIZED);
        this.status = TOKEN_STATUS;
    }
}

