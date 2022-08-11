package com.woowacourse.moragora.service;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.EventFixtures.EVENT3;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventResponse;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.dto.EventsResponse;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
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
        assertThatCode(() -> eventService.save(eventsRequest, meeting.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("모임 일정 전체를 조회한다.")
    @Test
    void find_all() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));

        final EventsResponse expectedEventsResponse = new EventsResponse(List.of(
                new EventResponse(
                        event1.getId(),
                        event1.getEntranceTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event1.getEntranceTime().plusMinutes(ATTENDANCE_END_INTERVAL),
                        event1.getEntranceTime(),
                        event1.getLeaveTime(),
                        event1.getDate()
                ),
                new EventResponse(
                        event2.getId(),
                        event2.getEntranceTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event2.getEntranceTime().plusMinutes(ATTENDANCE_END_INTERVAL),
                        event2.getEntranceTime(),
                        event2.getLeaveTime(),
                        event2.getDate()
                )
        ));

        // when
        final EventsResponse response = eventService.inquireByDuration(meeting.getId(), null, null);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedEventsResponse);
    }

    @DisplayName("특정 일정 이후의 일정을 조회한다.")
    @Test
    void find_isGreaterThanEqualBegin() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        final Event event3 = dataSupport.saveEvent(EVENT3.create(meeting));

        final EventsResponse expectedEventsResponse = new EventsResponse(List.of(
                new EventResponse(
                        event2.getId(),
                        event2.getEntranceTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event2.getEntranceTime().plusMinutes(ATTENDANCE_END_INTERVAL),
                        event2.getEntranceTime(),
                        event2.getLeaveTime(),
                        event2.getDate()
                ),
                new EventResponse(
                        event3.getId(),
                        event3.getEntranceTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event3.getEntranceTime().plusMinutes(ATTENDANCE_END_INTERVAL),
                        event3.getEntranceTime(),
                        event3.getLeaveTime(),
                        event3.getDate()
                )
        ));

        // when
        final EventsResponse response = eventService.inquireByDuration(meeting.getId(), event2.getDate(), null);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedEventsResponse);
    }

    @DisplayName("특정 일정 이전의 일정을 조회한다.")
    @Test
    void find_isLessThanEqualEnd() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveEvent(EVENT3.create(meeting));

        final EventsResponse expectedEventsResponse = new EventsResponse(List.of(
                new EventResponse(
                        event1.getId(),
                        event1.getEntranceTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event1.getEntranceTime().plusMinutes(ATTENDANCE_END_INTERVAL),
                        event1.getEntranceTime(),
                        event1.getLeaveTime(),
                        event1.getDate()
                ),
                new EventResponse(
                        event2.getId(),
                        event2.getEntranceTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event2.getEntranceTime().plusMinutes(ATTENDANCE_END_INTERVAL),
                        event2.getEntranceTime(),
                        event2.getLeaveTime(),
                        event2.getDate()
                )
        ));

        // when
        final EventsResponse response = eventService.inquireByDuration(meeting.getId(), null, event2.getDate());

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedEventsResponse);
    }

    @DisplayName("특정 기간 안의 일정을 조회한다.")
    @Test
    void find_inDuration() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveEvent(EVENT3.create(meeting));

        final EventsResponse expectedEventsResponse = new EventsResponse(List.of(
                new EventResponse(
                        event2.getId(),
                        event2.getEntranceTime().minusMinutes(ATTENDANCE_START_INTERVAL),
                        event2.getEntranceTime().plusMinutes(ATTENDANCE_END_INTERVAL),
                        event2.getEntranceTime(),
                        event2.getLeaveTime(),
                        event2.getDate()
                )
        ));

        // when
        final EventsResponse response = eventService
                .inquireByDuration(meeting.getId(), event2.getDate(), event2.getDate());

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedEventsResponse);
    }
}
