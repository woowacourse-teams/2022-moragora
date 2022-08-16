package com.woowacourse.moragora.service;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import com.woowacourse.moragora.support.ServerTimeManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private DataSupport dataSupport;

    @Autowired
    private ServerTimeManager serverTimeManager;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.execute();
    }

    @DisplayName("모임 일정들을 저장한다.")
    @Test
    void save() {
        // given
        final LocalDate date = LocalDate.of(2022, 8, 1);
        final LocalTime time = LocalTime.of(10, 0);
        final LocalDateTime now = LocalDateTime.of(date, time);
        serverTimeManager.refresh(now);

        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final Event event1 = new Event(date, time.plusHours(1), time.plusHours(2), meeting);
        final Event event2 = new Event(date.plusDays(1), time.plusHours(1), time.plusHours(2), meeting);
        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        EventRequest.builder()
                                .entranceTime(event1.getEntranceTime())
                                .leaveTime(event1.getLeaveTime())
                                .date(event1.getDate())
                                .build(),
                        EventRequest.builder()
                                .entranceTime(event2.getEntranceTime())
                                .leaveTime(event2.getLeaveTime())
                                .date(event2.getDate())
                                .build()
                )
        );

        // when, then
        assertThatCode(() -> eventService.save(eventsRequest, meeting.getId()))
                .doesNotThrowAnyException();
    }


    @DisplayName("오늘 이전의 이벤트를 생성하면 예외가 발생한다.")
    @Test
    void save_throwsException_ifEventDateNotPast() {
        // given
        final LocalDate date = LocalDate.of(2022, 8, 2);
        final LocalTime time = LocalTime.of(10, 0);
        final LocalDateTime now = LocalDateTime.of(date, time);
        serverTimeManager.refresh(now);

        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        EventRequest.builder()
                                .entranceTime(event1.getEntranceTime())
                                .leaveTime(event1.getLeaveTime())
                                .date(event1.getDate())
                                .build()
                        ,
                        EventRequest.builder()
                                .entranceTime(event2.getEntranceTime())
                                .leaveTime(event2.getLeaveTime())
                                .date(event2.getDate())
                                .build()
                ));

        // when, then
        assertThatThrownBy(() -> eventService.save(eventsRequest, meeting.getId()))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("오늘 이전의 이벤트를 생성할 수 없습니다.");
    }

    @DisplayName("같은 날에 복수의 이벤트가 존재하면 예외가 발생한다.")
    @Test
    void save_throwsException_ifDuplicatedEventDate() {
        // given
        final LocalDate date = LocalDate.of(2022, 8, 1);
        final LocalTime time = LocalTime.of(10, 0);
        final LocalDateTime now = LocalDateTime.of(date, time);
        serverTimeManager.refresh(now);

        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final Event event = EVENT1.create(meeting);
        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        EventRequest.builder()
                                .entranceTime(event.getEntranceTime())
                                .leaveTime(event.getLeaveTime())
                                .date(event.getDate())
                                .build(),
                        EventRequest.builder()
                                .entranceTime(event.getEntranceTime())
                                .leaveTime(event.getLeaveTime())
                                .date(event.getDate())
                                .build()
                )
        );

        // when, then
        assertThatThrownBy(() -> eventService.save(eventsRequest, meeting.getId()))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("하루에 복수의 일정을 생성할 수 없습니다.");
    }

    @DisplayName("오늘의 이벤트 생성 시, 이벤트 시작 시간이 출석 시작 시간보다 이후일 경우 예외가 발생한다.")
    @Test
    void save_throwsException_ifTodayEventStartTimeIsAfterAttendanceStartTime() {
        // given
        final LocalDate date = LocalDate.of(2022, 8, 1);
        final LocalTime time = LocalTime.of(9, 30, 1);
        final LocalDateTime now = LocalDateTime.of(date, time);
        serverTimeManager.refresh(now);

        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        EventRequest.builder()
                                .entranceTime(event1.getEntranceTime())
                                .leaveTime(event1.getLeaveTime())
                                .date(event1.getDate())
                                .build(),
                        EventRequest.builder()
                                .entranceTime(event2.getEntranceTime())
                                .leaveTime(event2.getLeaveTime())
                                .date(event2.getDate())
                                .build()
                )
        );

        // when, then
        assertThatThrownBy(() -> eventService.save(eventsRequest, meeting.getId()))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("출석 시간 전에 일정을 생성할 수 없습니다.");
    }

}
