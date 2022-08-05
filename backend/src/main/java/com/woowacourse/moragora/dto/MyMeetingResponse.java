package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MyMeetingResponse {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final LocalTime MEANINGLESS_TIME_DATA = LocalTime.MIDNIGHT;

    private final Long id;
    private final String name;
    private final Boolean isActive;
    private final String entranceTime;
    private final String closingTime;
    private final int tardyCount;
    private final Boolean isMaster;
    private final Boolean isCoffeeTime;
    private final Boolean hasUpcomingEvent;

    public MyMeetingResponse(final Long id,
                             final String name,
                             final Boolean isActive,
                             final LocalTime entranceTime,
                             final LocalTime closingTime,
                             final int tardyCount,
                             final Boolean isMaster,
                             final Boolean isCoffeeTime,
                             final Boolean hasUpcomingEvent) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.entranceTime = entranceTime.format(TIME_FORMATTER);
        this.closingTime = closingTime.format(TIME_FORMATTER);
        this.tardyCount = tardyCount;
        this.isMaster = isMaster;
        this.isCoffeeTime = isCoffeeTime;
        this.hasUpcomingEvent = hasUpcomingEvent;
    }

    public static MyMeetingResponse of(final Meeting meeting,
                                       final boolean isActive,
                                       final LocalTime closingTime,
                                       final int tardyCount,
                                       final Event event,
                                       final boolean isMaster,
                                       final boolean isCoffeeTime) {

        return new MyMeetingResponse(
                meeting.getId(),
                meeting.getName(),
                isActive,
                event.getEntranceTime(),
                closingTime,
                tardyCount,
                isMaster,
                isCoffeeTime,
                true
        );
    }

    public static MyMeetingResponse whenHasNoUpcomingEventOf(final Meeting meeting,
                                                             final boolean isActive,
                                                             final int tardyCount,
                                                             final boolean isMaster,
                                                             final boolean isCoffeeTime) {

        return new MyMeetingResponse(
                meeting.getId(),
                meeting.getName(),
                isActive,
                MEANINGLESS_TIME_DATA,
                MEANINGLESS_TIME_DATA,
                tardyCount,
                isMaster,
                isCoffeeTime,
                false
        );
    }
}
