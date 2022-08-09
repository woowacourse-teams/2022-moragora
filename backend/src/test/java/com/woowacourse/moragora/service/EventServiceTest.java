package com.woowacourse.moragora.service;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.woowacourse.moragora.dto.EventCancelRequest;
import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventsRequest;
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

    @DisplayName("모임 일정들을 삭제한다.")
    @Test
    void remove() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveEvent(EVENT2.create(meeting));

        final EventCancelRequest eventCancelRequest = new EventCancelRequest(
                List.of(EVENT1.getDate(), EVENT2.getDate()));

        // when, then
        assertThatCode(() -> eventService.delete(eventCancelRequest, meeting.getId()))
                .doesNotThrowAnyException();
    }
}
