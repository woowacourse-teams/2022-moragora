package com.woowacourse.moragora.exception.meeting;

public class IllegalStartEndDateException extends RuntimeException {

    private static final String MESSAGE = "시작 날짜보다 종료 날짜가 이를 수 없습니다.";

    public IllegalStartEndDateException() {
        super(MESSAGE);
    }
}
