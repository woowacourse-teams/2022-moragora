package com.woowacourse.moragora.dto.response.meeting;

import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.dto.response.event.EventResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MyMeetingResponse {

    private final Long id;
    private final String name;
    private final int tardyCount;
    private final Boolean isLoginUserMaster;
    private final Boolean isCoffeeTime;
    private final Boolean isActive;
    private final EventResponse upcomingEvent;

    @Builder
    public MyMeetingResponse(final Long id,
                             final String name,
                             final int tardyCount,
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
                                       final Long userId,
                                       final boolean isActive,
                                       final EventResponse upcomingEvent,
                                       final long tardyCount) {

        return new MyMeetingResponse(
                meeting.getId(),
                meeting.getName(),
                (int) tardyCount,
                meeting.isMaster(userId),
                meeting.isTardyStackFull(),
                isActive,
                upcomingEvent
        );
    }

    public static MyMeetingResponse of(final Long userId,
                                       final Meeting meeting,
                                       final Event upcomingEvent,
                                       final long tardyCount,
                                       final ServerTimeManager serverTimeManager) {
        final LocalDate today = serverTimeManager.getDate();
        boolean isActive = false;
        EventResponse eventResponse = null;
        if (upcomingEvent != null) {
            isActive = upcomingEvent.isActive(today, serverTimeManager);
            final LocalTime attendanceOpenTime = upcomingEvent.getOpenTime(serverTimeManager);
            final LocalTime attendanceClosedTime = upcomingEvent.getCloseTime(serverTimeManager);
            eventResponse = EventResponse.of(upcomingEvent, attendanceOpenTime, attendanceClosedTime);
        }

        return new MyMeetingResponse(
                meeting.getId(),
                meeting.getName(),
                (int) tardyCount,
                meeting.isMaster(userId),
                meeting.isTardyStackFull(),
                isActive,
                eventResponse
        );
    }
}
