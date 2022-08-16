package com.woowacourse.moragora.service;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.EventCancelRequest;
import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventResponse;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.event.EventNotFoundException;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
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
                                .meetingStartTime(event1.getStartTime())
                                .meetingEndTime(event1.getEndTime())
                                .date(event1.getDate())
                                .build(),
                        EventRequest.builder()
                                .meetingStartTime(event2.getStartTime())
                                .meetingEndTime(event2.getEndTime())
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
                                .meetingStartTime(event1.getStartTime())
                                .meetingEndTime(event1.getEndTime())
                                .date(event1.getDate())
                                .build()
                        ,
                        EventRequest.builder()
                                .meetingStartTime(event2.getStartTime())
                                .meetingEndTime(event2.getEndTime())
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
                                .meetingStartTime(event.getStartTime())
                                .meetingEndTime(event.getEndTime())
                                .date(event.getDate())
                                .build(),
                        EventRequest.builder()
                                .meetingStartTime(event.getStartTime())
                                .meetingEndTime(event.getEndTime())
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
        final Event event = EVENT1.create(meeting);
        final EventsRequest eventsRequest = new EventsRequest(List.of(
                EventRequest.builder()
                        .meetingStartTime(event.getStartTime())
                        .meetingEndTime(event.getEndTime())
                        .date(event.getDate())
                        .build()
        ));

        // when, then
        assertThatThrownBy(() -> eventService.save(eventsRequest, meeting.getId()))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("출석 시간 전에 일정을 생성할 수 없습니다.");
    }


    @DisplayName("모임 일정들을 삭제한다.")
    @Test
    void cancel() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveEvent(EVENT2.create(meeting));

        final EventCancelRequest eventCancelRequest = new EventCancelRequest(
                List.of(EVENT1.getDate(), EVENT2.getDate()));

        // when, then
        assertThatCode(() -> eventService.cancel(eventCancelRequest, meeting.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("없는 모임의 일정을 삭제요청 시 예외를 반환한다.")
    @Test
    void cancel_noMeeting() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveEvent(EVENT2.create(meeting));

        final EventCancelRequest eventCancelRequest = new EventCancelRequest(
                List.of(EVENT1.getDate(), EVENT2.getDate()));

        // when, then
        assertThatThrownBy(() -> eventService.cancel(eventCancelRequest, 2L))
                .isInstanceOf(MeetingNotFoundException.class);
    }

    @DisplayName("모임의 가장 가까운 일정을 조회한다.")
    @Test
    void findUpcomingEvent() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        dataSupport.saveEvent(event1);
        dataSupport.saveEvent(event2);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 30, 10, 6);
        serverTimeManager.refresh(dateTime);

        final EventResponse expected = EventResponse.of(
                event1, LocalTime.of(9, 30), LocalTime.of(10, 5));

        // when
        final EventResponse actual = eventService.findUpcomingEvent(meeting.getId());

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("모임의 가장 가까운 일정을 조회했을 때, 다음 일정이 존재하지 않으면 예외가 발생한다.")
    @Test
    void findUpcomingEvent_ifEventNotFound() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        dataSupport.saveEvent(event1);
        dataSupport.saveEvent(event2);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 3, 10, 6);
        serverTimeManager.refresh(dateTime);

        // when, then
        assertThatThrownBy(() -> eventService.findUpcomingEvent(meeting.getId()))
                .isInstanceOf(EventNotFoundException.class);
    }
}
