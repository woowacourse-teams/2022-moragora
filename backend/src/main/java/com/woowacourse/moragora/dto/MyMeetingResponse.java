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
    private final Boolean isActive;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String entranceTime;
    private final String leaveTime;
    private final String openingTime;
    private final String closingTime;
    private final int tardyCount;

    public MyMeetingResponse(final Long id,
                             final String name,
                             final Boolean isActive,
                             final LocalDate startDate,
                             final LocalDate endDate,
                             final LocalTime entranceTime,
                             final LocalTime leaveTime,
                             final LocalTime openingTime,
                             final LocalTime closingTime,
                             final int tardyCount) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entranceTime = entranceTime.format(TIME_FORMATTER);
        this.leaveTime = leaveTime.format(TIME_FORMATTER);
        this.openingTime = openingTime.format(TIME_FORMATTER);
        this.closingTime = closingTime.format(TIME_FORMATTER);
        this.tardyCount = tardyCount;
    }

    public static MyMeetingResponse of(final LocalTime now,
                                       final TimeChecker timeChecker,
                                       final Meeting meeting,
                                       final int tardyCount) {

        return new MyMeetingResponse(
                meeting.getId(),
                meeting.getName(),
                timeChecker.isAttendanceTime(now, meeting.getEntranceTime()),
                meeting.getStartDate(),
                meeting.getEndDate(),
                meeting.getEntranceTime(),
                meeting.getLeaveTime(),
                timeChecker.calculateOpeningTime(meeting.getEntranceTime()),
                timeChecker.calculateClosingTime(meeting.getEntranceTime()),
                tardyCount
        );
    }

}
