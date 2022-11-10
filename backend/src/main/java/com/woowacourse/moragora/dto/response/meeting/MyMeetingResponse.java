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
                                       final Event upcomingEvent,
                                       final Long loginUserId,
                                       final ServerTimeManager serverTimeManager) {

        final long tardyCount = meeting.tardyCountByUserId(loginUserId);

        final LocalDate today = serverTimeManager.getDate();

        if (upcomingEvent == null) {
            return createEmptyResponse(meeting, loginUserId, (int) tardyCount);
        }

        final LocalTime attendanceOpenTime = upcomingEvent.getOpenTime(serverTimeManager);
        final LocalTime attendanceClosedTime = upcomingEvent.getCloseTime(serverTimeManager);

        return new MyMeetingResponse(
                meeting.getId(),
                meeting.getName(),
                (int) tardyCount,
                meeting.isMaster(loginUserId),
                meeting.isTardyStackFull(),
                upcomingEvent.isActive(today, serverTimeManager),
                EventResponse.of(upcomingEvent, attendanceOpenTime, attendanceClosedTime)
        );
    }

    private static MyMeetingResponse createEmptyResponse(final Meeting meeting,
                                                         final Long loginUserId,
                                                         final int tardyCount) {
        return new MyMeetingResponse(
                meeting.getId(),
                meeting.getName(),
                tardyCount,
                meeting.isMaster(loginUserId),
                meeting.isTardyStackFull(),
                false,
                null
        );
    }
}
