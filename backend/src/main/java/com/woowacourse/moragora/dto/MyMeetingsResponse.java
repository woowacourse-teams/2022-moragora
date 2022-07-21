package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.service.closingstrategy.TimeChecker;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class MyMeetingsResponse {

    private final long serverTime;
    private final List<MyMeetingResponse> meetings;

    private MyMeetingsResponse(final LocalDateTime serverTime, final List<MyMeetingResponse> meetings) {
        this.serverTime = Timestamp.valueOf(serverTime).getTime();
        this.meetings = meetings;
    }

    public static MyMeetingsResponse of(final LocalDateTime now,
                                        final TimeChecker timeChecker,
                                        final List<Meeting> meetings) {
        final List<MyMeetingResponse> myMeetingResponses = meetings.stream()
                .map(meeting -> MyMeetingResponse.of(now.toLocalTime(), timeChecker, meeting))
                .collect(Collectors.toList());

        return new MyMeetingsResponse(now, myMeetingResponses);
    }
}
