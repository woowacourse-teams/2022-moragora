package com.woowacourse.moragora.exception;

public class ClosingTimeExcessException extends RuntimeException {

    private static final String MESSAGE = "마감시간을 넘어서 제출할 수 없습니다.";

    public ClosingTimeExcessException() {
        super(MESSAGE);
    }
}
