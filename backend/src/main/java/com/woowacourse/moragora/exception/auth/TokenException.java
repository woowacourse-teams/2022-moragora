package com.woowacourse.moragora.exception.auth;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import lombok.Getter;

@Getter
public class TokenException extends ClientRuntimeException {

    private final String status;

    public TokenException(final String message, final String status) {
        super(message, UNAUTHORIZED);
        this.status = status;
    }
}
