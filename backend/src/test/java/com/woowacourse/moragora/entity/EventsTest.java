package com.woowacourse.moragora.entity;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.EventFixtures.EVENT3;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EventsTest {

    @DisplayName("적용할 일정 중 수정을 실행하고 새로운 일정만 모아 반환한다.")
    @Test
    void updateAndExtractNewEvents() {
        // given
        final Meeting meeting = MORAGORA.create();

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        final Event event3 = EVENT3.create(meeting);

        final Events events = new Events(List.of(event1, event2));

        final List<Event> input = List.of(
                new Event(event1.getDate(), LocalTime.of(11, 0), event1.getEndTime(), meeting), event3);

        // when
        final List<Event> newEvents = events.updateAndExtractNewEvents(input);
        final List<Event> updatedEvents = events.getEvents();

        // then
        final Event newEvent3 = newEvents.get(0);
        final Event updatedEvent1 = updatedEvents.get(0);
        
        assertThat(newEvent3.getDate()).isEqualTo(event3.getDate());
        assertThat(updatedEvent1.getStartTime()).isEqualTo(LocalTime.of(11, 0));
    }
}
