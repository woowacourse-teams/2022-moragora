package com.woowacourse.moragora.exception.meeting;

import com.woowacourse.moragora.exception.global.NotFoundException;

public class AttendanceNotFoundException extends NotFoundException {

    private static final String MESSAGE = "출석 정보가 존재하지 않습니다.";

    public AttendanceNotFoundException() {
        super(MESSAGE);
    }
}
