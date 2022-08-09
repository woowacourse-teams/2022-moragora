package com.woowacourse.moragora.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EventResponse {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final String attendanceOpenTime;
    private final String attendanceClosedTime;
    private final String meetingStartTime;
    private final String meetingEndTime;
    private final LocalDate date;

    private EventResponse(final Long id,
                          final String attendanceOpenTime,
                          final String attendanceClosedTime,
                          final String meetingStartTime,
                          final String meetingEndTime,
                          final LocalDate date) {
        this.id = id;
        this.attendanceOpenTime = attendanceOpenTime;
        this.attendanceClosedTime = attendanceClosedTime;
        this.meetingStartTime = meetingStartTime;
        this.meetingEndTime = meetingEndTime;
        this.date = date;
    }

    public EventResponse(final Long id,
                         final LocalTime attendanceOpenTime,
                         final LocalTime attendanceClosedTime,
                         final LocalTime meetingStartTime,
                         final LocalTime meetingEndTime,
                         final LocalDate date) {
        this(id, attendanceOpenTime.format(TIME_FORMATTER), attendanceClosedTime.format(TIME_FORMATTER),
                meetingStartTime.format(TIME_FORMATTER), meetingEndTime.format(TIME_FORMATTER), date);
    }
}
