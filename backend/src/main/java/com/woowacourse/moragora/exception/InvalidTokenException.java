package com.woowacourse.moragora.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidTokenException extends ClientRuntimeException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";
    private static final String TOKEN_STATUS = "invalid";

    private final String status;

    public InvalidTokenException() {
        super(MESSAGE, HttpStatus.UNAUTHORIZED);
        this.status = TOKEN_STATUS;
    }
}