package com.woowacourse.moragora.exception.global;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class InvalidFormatException extends ClientRuntimeException {

    private static final String MESSAGE = "입력 형식이 올바르지 않습니다.";

    public InvalidFormatException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
