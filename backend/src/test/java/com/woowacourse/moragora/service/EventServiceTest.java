package com.woowacourse.moragora.service;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.EventFixtures.EVENT3;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.EventCancelRequest;
import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventResponse;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.dto.EventsResponse;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.event.EventNotFoundException;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import com.woowacourse.moragora.support.ServerTimeManager;
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

    private static final int ATTENDANCE_END_INTERVAL = 5;
    private static final int ATTENDANCE_START_INTERVAL = 30;

    @Autowired
    private EventService eventService;

    @Autowired
    private DataSupport dataSupport;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private ServerTimeManager serverTimeManager;

    @BeforeEach
    void setUp() {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.execute();
    }

    @DisplayName("모임 일정들을 저장한다.")
    @Test
    void save() {
        // given
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
        assertThatCode(() -> eventService.save(eventsRequest, meeting.getId()))
                .doesNotThrowAnyException();
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

    @DisplayName("모임 일정 전체를 조회한다.")
    @Test
    void findByDuration_all() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));

        final EventsResponse expectedEventsResponse = new EventsResponse(List.of(
                EventResponse.of(event1, event1.getStartTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event1.getStartTime().plusMinutes(ATTENDANCE_END_INTERVAL)),
                EventResponse.of(event2, event2.getStartTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event2.getStartTime().plusMinutes(ATTENDANCE_END_INTERVAL))
        ));

        // when
        final EventsResponse response = eventService.findByDuration(meeting.getId(), null, null);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedEventsResponse);
    }

    @DisplayName("특정 일정 이후의 일정을 조회한다.")
    @Test
    void findByDuration_isGreaterThanEqualBegin() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        final Event event3 = dataSupport.saveEvent(EVENT3.create(meeting));

        final EventsResponse expectedEventsResponse = new EventsResponse(List.of(
                EventResponse.of(event2, event2.getStartTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event2.getStartTime().plusMinutes(ATTENDANCE_END_INTERVAL)),
                EventResponse.of(event3, event3.getStartTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event3.getStartTime().plusMinutes(ATTENDANCE_END_INTERVAL))
        ));

        // when
        final EventsResponse response = eventService.findByDuration(meeting.getId(), event2.getDate(), null);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedEventsResponse);
    }

    @DisplayName("특정 일정 이전의 일정을 조회한다.")
    @Test
    void findByDuration_isLessThanEqualEnd() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveEvent(EVENT3.create(meeting));

        final EventsResponse expectedEventsResponse = new EventsResponse(List.of(
                EventResponse.of(event1, event1.getStartTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event1.getStartTime().plusMinutes(ATTENDANCE_END_INTERVAL)),
                EventResponse.of(event2, event2.getStartTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event2.getStartTime().plusMinutes(ATTENDANCE_END_INTERVAL))
        ));

        // when
        final EventsResponse response = eventService.findByDuration(meeting.getId(), null, event2.getDate());

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedEventsResponse);
    }

    @DisplayName("특정 기간 안의 일정을 조회한다.")
    @Test
    void findByDuration_inDuration() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveEvent(EVENT3.create(meeting));

        final EventsResponse expectedEventsResponse = new EventsResponse(List.of(
                EventResponse.of(event2, event2.getStartTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event2.getStartTime().plusMinutes(ATTENDANCE_END_INTERVAL))
        ));

        // when
        final EventsResponse response = eventService
                .findByDuration(meeting.getId(), event2.getDate(), event2.getDate());

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedEventsResponse);
    }

    @DisplayName("기간별 일정 조회시 조회하고자 하는 기간의 시작일이 종료일보다 늦는 경우 예외를 반환한다.")
    @Test
    void findByDuration_throwsExceptionIfBeginIsGreaterThanEnd() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));

        // when, then
        assertThatThrownBy(() -> eventService.findByDuration(meeting.getId(), event2.getDate(), event1.getDate()))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("기간의 입력이 잘못되었습니다.");
    }
}
