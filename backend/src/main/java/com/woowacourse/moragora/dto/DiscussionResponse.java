package com.woowacourse.moragora.dto;

import lombok.Getter;

@Getter
public class DiscussionResponse {

    private final long id;
    private final String title;
    private final String content;

    public DiscussionResponse(final long id, final String title, final String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
