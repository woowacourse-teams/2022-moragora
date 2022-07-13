package com.woowacourse.moragora.exception;

public class InvalidFormatException extends RuntimeException {

    private static final String MESSAGE = "입력 형식이 올바르지 않습니다.";

    public InvalidFormatException() {
        super(MESSAGE);
    }
}
