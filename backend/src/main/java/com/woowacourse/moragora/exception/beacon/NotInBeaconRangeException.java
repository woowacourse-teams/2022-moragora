package com.woowacourse.moragora.exception.beacon;

import com.woowacourse.moragora.exception.global.BadRequestException;

public class NotInBeaconRangeException extends BadRequestException {

    private static final String MESSAGE = "비콘의 출석 반경 이내에 있지 않습니다.";

    public NotInBeaconRangeException() {
        super(MESSAGE);
    }
}
