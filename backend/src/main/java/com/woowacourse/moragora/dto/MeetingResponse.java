package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;

@Getter
public class MeetingResponse {

    private final Long id;
    private final String name;
    private final long attendanceCount;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime entranceTime;
    private final LocalTime leaveTime;
    private final List<UserResponse> users;

    public MeetingResponse(final Long id,
                           final String name,
                           final long attendanceCount,
                           final LocalDate startDate,
                           final LocalDate endDate,
                           final LocalTime entranceTime,
                           final LocalTime leaveTime,
                           final List<UserResponse> usersResponse) {
        this.id = id;
        this.name = name;
        this.attendanceCount = attendanceCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entranceTime = entranceTime;
        this.leaveTime = leaveTime;
        this.users = usersResponse;
    }

    public static MeetingResponse of(final Meeting meeting, final List<UserResponse> userResponses,
                                     final long meetingAttendanceCount) {
        return new MeetingResponse(
                meeting.getId(),
                meeting.getName(),
                meetingAttendanceCount,
                meeting.getStartDate(),
                meeting.getEndDate(),
                meeting.getEntranceTime(),
                meeting.getLeaveTime(),
                userResponses
        );
    }
}
