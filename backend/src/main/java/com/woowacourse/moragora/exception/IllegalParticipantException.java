package com.woowacourse.moragora.exception;

public class IllegalParticipantException extends RuntimeException {

    private static final String MESSAGE = "참가자 정보가 잘못되었습니다.";

    public IllegalParticipantException() {
        super(MESSAGE);
    }
}
