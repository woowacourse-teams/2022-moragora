package com.woowacourse.moragora.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MyMeetingsResponse {

    private final long serverTime;
    private final List<MyMeetingResponse> meetings;

    private MyMeetingsResponse(final long serverTime, final List<MyMeetingResponse> meetings) {
        this.serverTime = serverTime;
        this.meetings = meetings;
    }

    public static MyMeetingsResponse of(final LocalDateTime now, final List<MyMeetingResponse> meetings) {
        return new MyMeetingsResponse(toTimestamp(now), meetings);
    }

    private static long toTimestamp(final LocalDateTime now) {
        return Timestamp.valueOf(now).getTime();
    }
}
