package com.woowacourse.moragora.exception;

import org.springframework.http.HttpStatus;

public class IllegalStartEndDateException extends ClientRuntimeException {

    private static final String MESSAGE = "시작 날짜보다 종료 날짜가 이를 수 없습니다.";

    public IllegalStartEndDateException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
