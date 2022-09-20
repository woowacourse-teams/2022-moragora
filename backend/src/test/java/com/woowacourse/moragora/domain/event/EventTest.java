package com.woowacourse.moragora.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.exception.meeting.IllegalEntranceLeaveTimeException;
import com.woowacourse.moragora.support.fixture.EventFixtures;
import com.woowacourse.moragora.support.fixture.MeetingFixtures;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventTest {

    @DisplayName("같은 날짜의 이벤트인지 확인한다.")
    @Test
    void isSameDate() {
        // given
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Event event = new Event(
                LocalDate.of(2022, 8, 1),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                meeting);

        // when
        final boolean actual = event.isSameDate(LocalDate.of(2022, 8, 1));

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("이벤트를 가지고 있는 미팅이 같은지 확인한다.")
    @Test
    void isSameMeeting() {
        // given
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Event event = EventFixtures.EVENT1.create(meeting);

        // when
        final boolean actual = event.isSameMeeting(meeting);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("이벤트의 시간을 업데이트한다.")
    @Test
    void updateTime() {
        // given
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Event event = EventFixtures.EVENT1.create(meeting);

        // when
        event.updateTime(LocalTime.of(10, 5), LocalTime.of(11, 5));

        // then
        assertAll(
                () -> assertThat(event.getStartTime()).isEqualTo(LocalTime.of(10, 5)),
                () -> assertThat(event.getEndTime()).isEqualTo(LocalTime.of(11, 5))
        );
    }

    @DisplayName("이벤트가 이전 날짜인지 확인한다.")
    @Test
    void isDateBefore() {
        // given
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Event event = new Event(
                LocalDate.of(2022, 8, 1),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                meeting);

        // when
        final boolean actual = event.isDateBefore(LocalDate.of(2022, 8, 2));

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("이벤트를 업데이트하려고 할 때 시작시간이 종료시간보다 이후의 시간이라면 예외가 발생한다.")
    @Test
    void update_throwException_ifStartTimeIsAfterLeaveTime() {
        // given
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Event event = EventFixtures.EVENT1.create(meeting);

        // when, then
        assertThatThrownBy(() -> event.updateTime(LocalTime.of(11, 5), LocalTime.of(10, 5)))
                .isInstanceOf(IllegalEntranceLeaveTimeException.class);
    }
}
