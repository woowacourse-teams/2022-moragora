package com.woowacourse.moragora.exception.user;

import com.woowacourse.moragora.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    private static final String MESSAGE = "유저가 존재하지 않습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
