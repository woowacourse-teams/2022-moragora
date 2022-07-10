package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class MeetingResponse {

    private final Long id;
    private final String name;
    private final int attendanceCount;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime entranceTime;
    private final LocalTime leaveTime;

    public MeetingResponse(final Long id,
                           final String name,
                           final int attendanceCount,
                           final LocalDate startDate,
                           final LocalDate endDate,
                           final LocalTime entranceTime,
                           final LocalTime leaveTime) {
        this.id = id;
        this.name = name;
        this.attendanceCount = attendanceCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entranceTime = entranceTime;
        this.leaveTime = leaveTime;
    }

    public static MeetingResponse from(final Meeting meeting) {
        return new MeetingResponse(
                meeting.getId(),
                meeting.getName(),
                meeting.getAttendanceCount(),
                meeting.getStartDate(),
                meeting.getEndDate(),
                meeting.getEntranceTime(),
                meeting.getLeaveTime());
    }
}
