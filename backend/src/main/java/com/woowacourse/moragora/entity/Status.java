package com.woowacourse.moragora.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;

public enum Status {

    PRESENT, TARDY, NONE;

    @JsonCreator
    public static Status fromEnum(final String value) {
        return Arrays.stream(Status.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findAny()
                .orElseThrow(() -> new ClientRuntimeException("존재하지 않는 상태입니다.", HttpStatus.BAD_REQUEST));
    }
}
