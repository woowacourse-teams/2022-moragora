package com.woowacourse.moragora.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class MyMeetingResponse {

    private final Long id;
    private final String name;
    private final boolean active;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime entranceTime;
    private final LocalTime closingTime;

    public MyMeetingResponse(final Long id,
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
        this.entranceTime = entranceTime;
        this.closingTime = closingTime;
    }
}
