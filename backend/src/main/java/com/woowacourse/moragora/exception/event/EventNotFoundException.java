package com.woowacourse.moragora.exception.event;

import com.woowacourse.moragora.exception.global.NotFoundException;

public class EventNotFoundException extends NotFoundException {

    private static final String MESSAGE = "일정이 존재하지 않습니다.";

    public EventNotFoundException() {
        super(MESSAGE);
    }
}
