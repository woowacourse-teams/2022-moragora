package com.woowacourse.moragora.exception.meeting;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class NotCheckInTimeException extends ClientRuntimeException {

    private static final String MESSAGE = "출석 가능한 시간이 아닙니다.";

    public NotCheckInTimeException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
