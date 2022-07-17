package com.woowacourse.moragora.exception;

public class NoKeywordException extends RuntimeException {

    private static final String MESSAGE = "검색어가 입력되지 않았습니다.";

    public NoKeywordException() {
        super(MESSAGE);
    }
}
