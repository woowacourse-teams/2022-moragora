package com.woowacourse.moragora.exception.user;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends ClientRuntimeException {

    private static final String MESSAGE = "비밀번호가 올바르지 않습니다.";

    public InvalidPasswordException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
