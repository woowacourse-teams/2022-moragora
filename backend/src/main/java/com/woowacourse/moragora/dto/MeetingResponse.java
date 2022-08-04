package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.MeetingAttendances;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MeetingResponse {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final String name;
    private final long attendanceCount;
    private final Boolean isActive;
    private final Boolean isMaster;
    private final Boolean isCoffeeTime;
    private final Boolean hasUpcomingEvent;
    private final List<ParticipantResponse> users;

    @Builder
    public MeetingResponse(final Long id,
                           final String name,
                           final long attendanceCount,
                           final Boolean isActive,
                           final Boolean isMaster,
                           final Boolean isCoffeeTime,
                           final Boolean hasUpcomingEvent,
                           final List<ParticipantResponse> usersResponse) {
        this.id = id;
        this.name = name;
        this.attendanceCount = attendanceCount;
        this.isActive = isActive;
        this.isMaster = isMaster;
        this.isCoffeeTime = isCoffeeTime;
        this.hasUpcomingEvent = hasUpcomingEvent;
        this.users = usersResponse;
    }

    public static MeetingResponse of(final Meeting meeting,
                                     final boolean isMaster,
                                     final boolean isActive,
                                     final List<ParticipantResponse> participantResponses,
                                     final MeetingAttendances meetingAttendances,
                                     final boolean hasUpcomingEvent,
                                     final int attendanceCount) {
        return new MeetingResponse(
                meeting.getId(),
                meeting.getName(),
                attendanceCount,
                isActive,
                isMaster,
                meetingAttendances.isTardyStackFull(),
                hasUpcomingEvent,
                participantResponses
        );
    }
}
