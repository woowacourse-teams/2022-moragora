package com.woowacourse.moragora.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MyMeetingsResponse {

    private final List<MyMeetingResponse> meetings;

    public MyMeetingsResponse(final List<MyMeetingResponse> meetings) {
        this.meetings = meetings;
    }
}
