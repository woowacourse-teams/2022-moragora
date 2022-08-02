package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
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
    private final String closingTime;
    private final int tardyCount;
    private final Boolean isCoffeeTime;

    public MyMeetingResponse(final Long id,
                             final String name,
                             final Boolean isActive,
                             final LocalDate startDate,
                             final LocalDate endDate,
                             final LocalTime entranceTime,
                             final LocalTime closingTime,
                             final int tardyCount,
                             final Boolean isCoffeeTime) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entranceTime = entranceTime.format(TIME_FORMATTER);
        this.closingTime = closingTime.format(TIME_FORMATTER);
        this.tardyCount = tardyCount;
        this.isCoffeeTime = isCoffeeTime;
    }

    public static MyMeetingResponse of(final Meeting meeting,
                                       final boolean isActive,
                                       final LocalTime closingTime,
                                       final int tardyCount,
                                       final boolean isCoffeeTime) {

        return new MyMeetingResponse(
                meeting.getId(),
                meeting.getName(),
                isActive,
                meeting.getStartDate(),
                meeting.getEndDate(),
                meeting.getEntranceTime(),
                closingTime,
                tardyCount,
                isCoffeeTime
        );
    }

}
