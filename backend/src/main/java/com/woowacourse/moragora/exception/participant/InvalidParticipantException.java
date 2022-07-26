package com.woowacourse.moragora.exception.participant;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.springframework.http.HttpStatus;

public class InvalidParticipantException extends ClientRuntimeException {

    public InvalidParticipantException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
