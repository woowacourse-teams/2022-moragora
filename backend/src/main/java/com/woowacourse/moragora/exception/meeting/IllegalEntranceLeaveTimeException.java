package com.woowacourse.moragora.exception.meeting;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class IllegalEntranceLeaveTimeException extends ClientRuntimeException {

    private static final String MESSAGE = "시작 시간보다 종료 시간이 이를 수 없습니다.";

    public IllegalEntranceLeaveTimeException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
