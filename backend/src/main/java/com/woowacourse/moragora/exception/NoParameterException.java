package com.woowacourse.moragora.exception;

public class NoParameterException extends RuntimeException {

    private static final String MESSAGE = "값이 입력되지 않았습니다.";

    public NoParameterException() {
        super(MESSAGE);
    }
}
