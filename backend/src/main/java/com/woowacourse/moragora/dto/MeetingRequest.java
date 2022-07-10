package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MeetingRequest {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime entranceTime;
    private LocalTime leaveTime;

    public MeetingRequest(final String name, final LocalDate startDate, final LocalDate endDate,
                          final LocalTime entranceTime, final LocalTime leaveTime) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entranceTime = entranceTime;
        this.leaveTime = leaveTime;
    }

    public Meeting toEntity() {
        return new Meeting(name, startDate, endDate, entranceTime, leaveTime);
    }
}
