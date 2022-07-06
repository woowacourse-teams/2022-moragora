package com.woowacourse.moragora.exception;

public class DiscussionNotFoundException extends RuntimeException {

    private static final String MESSAGE = "토론 게시글이 존재하지 않습니다.";

    public DiscussionNotFoundException() {
        super(MESSAGE);
    }
}
