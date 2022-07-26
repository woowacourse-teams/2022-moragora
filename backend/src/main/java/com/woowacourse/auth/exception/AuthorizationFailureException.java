package com.woowacourse.auth.exception;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class AuthorizationFailureException extends ClientRuntimeException {

    private static final String MESSAGE = "이메일이나 비밀번호가 틀렸습니다.";

    public AuthorizationFailureException() {
        super(MESSAGE, HttpStatus.UNAUTHORIZED);
    }
}
