package com.woowacourse.moragora.exception.auth;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class AuthCodeException extends ClientRuntimeException {

    public AuthCodeException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
