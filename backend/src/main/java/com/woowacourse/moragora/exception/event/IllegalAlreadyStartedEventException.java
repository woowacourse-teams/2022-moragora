package com.woowacourse.moragora.exception.event;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class IllegalAlreadyStartedEventException extends ClientRuntimeException {

    private static final String MESSAGE = "일정이 시작된 후에는 변경할 수 없습니다.";

    public IllegalAlreadyStartedEventException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
