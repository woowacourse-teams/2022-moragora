package com.woowacourse.moragora.exception.user;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class EmailDuplicatedException extends ClientRuntimeException {

    private static final String MESSAGE = "이미 존재하는 이메일입니다.";

    public EmailDuplicatedException() {
        super(MESSAGE, HttpStatus.CONFLICT);
    }
}
