package com.woowacourse.moragora.dto.response;

import lombok.Getter;

@Getter
public class TokenErrorResponse extends ErrorResponse {

    private final String tokenStatus;

    public TokenErrorResponse(final String message, final String tokenStatus) {
        super(message);
        this.tokenStatus = tokenStatus;
    }
}
