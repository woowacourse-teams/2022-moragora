package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.service.closingstrategy.TimeChecker;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class MyMeetingResponse {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final String name;
    private final boolean active;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String entranceTime;
    private final String closingTime;

    private MyMeetingResponse(final Long id,
                              final String name,
                              final boolean active,
                              final LocalDate startDate,
                              final LocalDate endDate,
                              final LocalTime entranceTime,
                              final LocalTime closingTime) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entranceTime = entranceTime.format(TIME_FORMATTER);
        this.closingTime = closingTime.format(TIME_FORMATTER);
    }

    public static MyMeetingResponse of(final LocalTime now, final TimeChecker timeChecker, final Meeting meeting) {

        return new MyMeetingResponse(
                meeting.getId(),
                meeting.getName(),
                timeChecker.isAttendanceTime(now, meeting.getEntranceTime()),
                meeting.getStartDate(),
                meeting.getEndDate(),
                meeting.getEntranceTime(),
                timeChecker.calculateClosingTime(meeting.getEntranceTime())
        );
    }
}
