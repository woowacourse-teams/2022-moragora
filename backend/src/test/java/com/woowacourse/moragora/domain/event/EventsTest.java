package com.woowacourse.moragora.domain.event;

import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT3;
import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.moragora.domain.meeting.Meeting;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventsTest {

    @DisplayName("적용할 일정 중 수정을 실행하고 새로운 일정만 모아 반환한다.")
    @Test
    void updateAndExtractNewEvents() {
        // given
        final Meeting meeting = MORAGORA.create();

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        final Events events = new Events(List.of(event1, event2));

        final LocalTime changedTime = LocalTime.of(11, 0);
        final Event updatedEvent1 = new Event(event1.getDate(), changedTime, event1.getEndTime(), meeting);
        final Event newEvent = EVENT3.create(meeting);

        final List<Event> inputEvents = List.of(updatedEvent1, newEvent);

        // when
        final List<Event> newEvents = events.updateAndExtractNewEvents(inputEvents);

        // then
        assertAll(
                () -> assertThat(newEvents).hasSize(1),
                () -> assertThat(event1).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(updatedEvent1)
        );
    }
}
