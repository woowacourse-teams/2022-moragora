package com.woowacourse.moragora.dto;

import java.time.LocalTime;
import java.util.List;
import lombok.Getter;

@Getter
public class MyMeetingsResponse {

    private final LocalTime serverTime;
    private final List<MyMeetingResponse> meetings;

    public MyMeetingsResponse(final LocalTime serverTime, final List<MyMeetingResponse> meetings) {
        this.serverTime = serverTime;
        this.meetings = meetings;
    }
}
