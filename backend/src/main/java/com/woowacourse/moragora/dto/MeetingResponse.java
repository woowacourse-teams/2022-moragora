package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.MeetingAttendances;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MeetingResponse {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final String name;
    private final long attendanceCount;
    private final Boolean isMaster;
    private final Boolean isCoffeeTime;
    private final List<ParticipantResponse> users;

    public MeetingResponse(final Long id,
                           final String name,
                           final long attendanceCount,
                           final boolean isMaster,
                           final Boolean isCoffeeTime,
                           final List<ParticipantResponse> usersResponse) {
        this.id = id;
        this.name = name;
        this.attendanceCount = attendanceCount;
        this.isMaster = isMaster;
        this.isCoffeeTime = isCoffeeTime;
        this.users = usersResponse;
    }

    public static MeetingResponse of(final Meeting meeting,
                                     final boolean isMaster,
                                     final List<ParticipantResponse> participantResponses,
                                     final MeetingAttendances meetingAttendances,
                                     final int attendanceCount) {
        return new MeetingResponse(
                meeting.getId(),
                meeting.getName(),
                attendanceCount,
                isMaster,
                meetingAttendances.isTardyStackFull(),
                participantResponses
        );
    }
}
