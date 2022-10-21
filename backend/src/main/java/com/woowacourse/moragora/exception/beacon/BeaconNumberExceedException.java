package com.woowacourse.moragora.exception.beacon;

import com.woowacourse.moragora.exception.global.BadRequestException;

public class BeaconNumberExceedException extends BadRequestException {

    private static final String MESSAGE = "비콘의 수는 3을 초과할 수 없습니다.";

    public BeaconNumberExceedException() {
        super(MESSAGE);
    }
}
