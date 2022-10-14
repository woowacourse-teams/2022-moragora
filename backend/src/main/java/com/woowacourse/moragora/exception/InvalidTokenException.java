package com.woowacourse.moragora.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidTokenException extends ClientRuntimeException {

    private final String status;

    public InvalidTokenException(final String status, final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
        this.status = status;
    }

    public static InvalidTokenException ofInvalid() {
        return new InvalidTokenException("invalid", "유효하지 않은 토큰입니다.");
    }
}
