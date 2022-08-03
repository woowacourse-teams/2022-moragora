package com.woowacourse.moragora.exception;

import org.springframework.http.HttpStatus;

public class InvalidCoffeeTimeException extends ClientRuntimeException {

    private static final String MESSAGE = "커피 스택이 충분히 쌓이지 않았습니다.";

    public InvalidCoffeeTimeException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
