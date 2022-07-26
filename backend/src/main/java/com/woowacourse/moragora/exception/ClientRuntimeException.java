package com.woowacourse.moragora.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientRuntimeException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ClientRuntimeException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
