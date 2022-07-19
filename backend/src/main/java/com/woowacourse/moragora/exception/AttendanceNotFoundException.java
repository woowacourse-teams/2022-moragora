package com.woowacourse.moragora.exception;

public class AttendanceNotFoundException extends RuntimeException {

    private static final String MESSAGE = "출석 정보가 존재하지 않습니다.";

    public AttendanceNotFoundException() {
        super(MESSAGE);
    }
}
