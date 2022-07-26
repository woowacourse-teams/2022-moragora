package com.woowacourse.moragora.exception.meeting;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class ClosingTimeExcessException extends ClientRuntimeException {

    private static final String MESSAGE = "마감시간을 넘어서 제출할 수 없습니다.";

    public ClosingTimeExcessException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
