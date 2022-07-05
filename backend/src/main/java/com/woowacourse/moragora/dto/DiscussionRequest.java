package com.woowacourse.moragora.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DiscussionRequest {

    private String title;
    private String content;

    public DiscussionRequest(final String title, final String content) {
        this.title = title;
        this.content = content;
    }
}
