package com.woowacourse.moragora.dto.response.meeting;

import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.dto.response.event.EventResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MyMeetingResponse {

    private final Long id;
    private final String name;
    private final Integer tardyCount;
    private final Boolean isLoginUserMaster;
    private final Boolean isCoffeeTime;
    private final Boolean isActive;
    private final EventResponse upcomingEvent;

    @Builder
    public MyMeetingResponse(final Long id,
                             final String name,
                             final Integer tardyCount,
                             final Boolean isLoginUserMaster,
                             final Boolean isCoffeeTime,
                             final Boolean isActive,
                             final EventResponse upcomingEvent) {
        this.id = id;
        this.name = name;
        this.tardyCount = tardyCount;
        this.isLoginUserMaster = isLoginUserMaster;
        this.isCoffeeTime = isCoffeeTime;
        this.isActive = isActive;
        this.upcomingEvent = upcomingEvent;
    }

    public static MyMeetingResponse of(final Meeting meeting,
                                       final Integer tardyCount,
                                       final boolean isLoginUserMaster,
                                       final boolean isCoffeeTime,
                                       final boolean isActive,
                                       final EventResponse upcomingEvent) {

        return new MyMeetingResponse(
                meeting.getId(), meeting.getName(),
                tardyCount, isLoginUserMaster, isCoffeeTime, isActive,
                upcomingEvent
        );
    }
}
