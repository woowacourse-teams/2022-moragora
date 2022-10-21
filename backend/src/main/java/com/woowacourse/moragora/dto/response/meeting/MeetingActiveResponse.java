package com.woowacourse.moragora.dto.response.meeting;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MeetingActiveResponse {

    private final Boolean isActive;

    public MeetingActiveResponse(final boolean isActive) {
        this.isActive = isActive;
    }
}
