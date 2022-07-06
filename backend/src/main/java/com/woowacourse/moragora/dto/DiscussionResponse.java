package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Discussion;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscussionResponse {

    private final long id;
    private final String title;
    private final String content;
    private final int views;
    private final long createdAt;
    private final long updatedAt;

    public DiscussionResponse(final long id, final String title, final String content, final int views,
                              final long createdAt, final long updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.views = views;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DiscussionResponse from(final Discussion discussion) {
        return new DiscussionResponse(discussion.getId(), discussion.getTitle(), discussion.getContent(),
                discussion.getViews(), toTimestamp(discussion.getCreatedAt()), toTimestamp(discussion.getUpdatedAt()));
    }

    private static long toTimestamp(final LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime).getTime();
    }
}
