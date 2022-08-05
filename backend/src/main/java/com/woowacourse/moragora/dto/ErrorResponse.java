package com.woowacourse.moragora.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse {

    private final String message;

    public ErrorResponse(final String message) {
        this.message = message;
    }
}
