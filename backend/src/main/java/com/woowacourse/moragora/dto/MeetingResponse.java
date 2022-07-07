package com.woowacourse.moragora.dto;

import lombok.Getter;

@Getter
public class MeetingResponse {

    private final Long id;
    private final int meetingCount;

    public MeetingResponse(final Long id, final int meetingCount) {
        this.id = id;
        this.meetingCount = meetingCount;
    }
}
