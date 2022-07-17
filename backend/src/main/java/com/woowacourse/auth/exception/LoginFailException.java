package com.woowacourse.auth.exception;

public class LoginFailException extends RuntimeException {

    private static final String MESSAGE = "이메일이나 비밀번호가 틀렸습니다.";

    public LoginFailException() {
        super(MESSAGE);
    }
}
