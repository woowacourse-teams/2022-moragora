package com.woowacourse.moragora.exception.user;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class AuthenticationFailureException extends ClientRuntimeException {

    private static final String MESSAGE = "이메일이나 비밀번호가 틀렸습니다.";

    public AuthenticationFailureException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
