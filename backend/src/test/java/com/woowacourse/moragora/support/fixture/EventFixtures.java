package com.woowacourse.moragora.support.fixture;

import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import java.time.LocalDate;
import java.time.LocalTime;

public enum EventFixtures {

    EVENT1(LocalDate.of(2022, 8, 1), LocalTime.of(10, 0), LocalTime.of(18, 0)),
    EVENT2(LocalDate.of(2022, 8, 2), LocalTime.of(10, 0), LocalTime.of(18, 0)),
    EVENT3(LocalDate.of(2022, 8, 3), LocalTime.of(10, 0), LocalTime.of(18, 0)),
    EVENT4(LocalDate.of(2022, 8, 4), LocalTime.of(10, 0), LocalTime.of(18, 0)),
    EVENT_WITHOUT_DATE(null, LocalTime.of(10, 0), LocalTime.of(18, 0));

    private final LocalDate date;

    private final LocalTime entranceTime;

    private final LocalTime leaveTime;

    EventFixtures(final LocalDate date, final LocalTime entranceTime, final LocalTime leaveTime) {
        this.date = date;
        this.entranceTime = entranceTime;
        this.leaveTime = leaveTime;
    }

    public Event create(final Meeting meeting) {
        return Event.builder()
                .date(date)
                .startTime(entranceTime)
                .endTime(leaveTime)
                .meeting(meeting)
                .build();
    }

    public Event createEventOnDate(final Meeting meeting, final LocalDate date) {
        return Event.builder()
                .date(date)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.MAX)
                .meeting(meeting)
                .build();
    }

    public Event createEventOnDateAndTime(final Meeting meeting, final LocalDate date, final LocalTime time) {
        return Event.builder()
                .date(date)
                .startTime(time)
                .endTime(LocalTime.MAX)
                .meeting(meeting)
                .build();
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getEntranceTime() {
        return entranceTime;
    }

    public LocalTime getLeaveTime() {
        return leaveTime;
    }
}
