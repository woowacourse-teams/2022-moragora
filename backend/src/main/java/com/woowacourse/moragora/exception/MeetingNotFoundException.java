package com.woowacourse.moragora.exception;

public class MeetingNotFoundException extends NotFoundException {

    private static final String MESSAGE = "미팅 방이 존재하지 않습니다";

    public MeetingNotFoundException() {
        super(MESSAGE);
    }
}
