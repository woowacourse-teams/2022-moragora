package com.woowacourse.moragora.domain.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(final String message) {
        super(message);
    }
}
