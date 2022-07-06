package com.woowacourse.moragora.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class DiscussionsResponse {

    private final List<DiscussionResponse> discussions;

    public DiscussionsResponse(final List<DiscussionResponse> discussionResponses) {
        this.discussions = discussionResponses;
    }
}
