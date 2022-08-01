package com.woowacourse.moragora.support;

import com.woowacourse.moragora.entity.Meeting;
import java.time.LocalDate;
import java.time.LocalTime;

public enum MeetingFixtures {

    MORAGORA("모라고라",
            LocalDate.of(2022, 8, 1),
            LocalDate.of(2022, 8, 19),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)),
    SOKDAKSOKDAK("속닥속닥",
            LocalDate.of(2022, 8, 1),
            LocalDate.of(2022, 8, 19),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)),
    F12("F12", LocalDate.of(2022, 8, 1),
            LocalDate.of(2022, 8, 19),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)),
    TEATIME("티타임",
            LocalDate.of(2022, 8, 1),
            LocalDate.of(2022, 8, 19),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0));

    private final String name;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final LocalTime entranceTime;

    private final LocalTime leaveTime;

    MeetingFixtures(final String name, final LocalDate startDate, final LocalDate endDate,
                    final LocalTime entranceTime, final LocalTime leaveTime) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entranceTime = entranceTime;
        this.leaveTime = leaveTime;
    }

    public Meeting create() {
        return Meeting.builder()
                .name(this.name)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .entranceTime(this.entranceTime)
                .leaveTime(this.leaveTime)
                .build();
    }
}
