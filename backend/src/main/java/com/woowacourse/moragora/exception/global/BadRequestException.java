package com.woowacourse.moragora.exception.global;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ClientRuntimeException {

    public BadRequestException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
