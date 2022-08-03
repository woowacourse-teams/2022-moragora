package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.MeetingAttendances;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MeetingResponse {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final String name;
    private final long attendanceCount;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String entranceTime;
    private final String leaveTime;
    private final Boolean isMaster;
    private final Boolean isCoffeeTime;
    private final List<ParticipantResponse> users;

    @Builder
    public MeetingResponse(final Long id,
                           final String name,
                           final long attendanceCount,
                           final LocalDate startDate,
                           final LocalDate endDate,
                           final LocalTime entranceTime,
                           final LocalTime leaveTime,
                           final boolean isMaster,
                           final Boolean isCoffeeTime,
                           final List<ParticipantResponse> usersResponse) {
        this.id = id;
        this.name = name;
        this.attendanceCount = attendanceCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entranceTime = entranceTime.format(TIME_FORMATTER);
        this.leaveTime = leaveTime.format(TIME_FORMATTER);
        this.isMaster = isMaster;
        this.isCoffeeTime = isCoffeeTime;
        this.users = usersResponse;
    }

    public static MeetingResponse of(final Meeting meeting,
                                     final boolean isMaster,
                                     final List<ParticipantResponse> participantResponses,
                                     final MeetingAttendances meetingAttendances) {

        return MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .attendanceCount(meetingAttendances.countProceedDate())
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .entranceTime(meeting.getEntranceTime())
                .leaveTime(meeting.getLeaveTime())
                .isMaster(isMaster)
                .isCoffeeTime(meetingAttendances.isTardyStackFull())
                .usersResponse(participantResponses)
                .build();
    }
}
