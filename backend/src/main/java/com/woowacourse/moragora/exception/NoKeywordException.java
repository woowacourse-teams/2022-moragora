package com.woowacourse.moragora.exception;

import org.springframework.http.HttpStatus;

public class NoKeywordException extends ClientRuntimeException {

    private static final String MESSAGE = "검색어가 입력되지 않았습니다.";

    public NoKeywordException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
