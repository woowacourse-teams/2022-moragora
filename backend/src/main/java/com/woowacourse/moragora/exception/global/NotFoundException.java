package com.woowacourse.moragora.exception.global;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ClientRuntimeException {

    public NotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
