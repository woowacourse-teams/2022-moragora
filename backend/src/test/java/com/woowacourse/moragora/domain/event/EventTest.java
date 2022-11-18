package com.woowacourse.moragora.domain.event;

import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.exception.event.IllegalAlreadyStartedEventException;
import com.woowacourse.moragora.exception.meeting.IllegalEntranceLeaveTimeException;
import com.woowacourse.moragora.support.fixture.ServerDateTimeFixtures;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventTest {

    @DisplayName("이벤트 생성 시 시작시간이 종료시간보다 이후의 시간이라면 예외가 발생한다.")
    @Test
    void construct_throwException_ifStartTimeIsAfterLeaveTime() {
        // given
        final Meeting meeting = MORAGORA.create();

        // when, then
        assertThatThrownBy(() -> new Event(LocalDate.now(), LocalTime.of(11, 5),
                LocalTime.of(10, 5), meeting))
                .isInstanceOf(IllegalEntranceLeaveTimeException.class);
    }

    @DisplayName("같은 날짜의 이벤트인지 확인한다.")
    @Test
    void isSameDate() {
        // given
        final Meeting meeting = MORAGORA.create();
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
        final Meeting meeting = MORAGORA.create();
        final Event event = EVENT1.create(meeting);

        // when
        final boolean actual = event.isSameMeeting(meeting);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("이벤트가 이전 날짜인지 확인한다.")
    @Test
    void isDateBefore() {
        // given
        final Meeting meeting = MORAGORA.create();
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

    @DisplayName("이벤트의 시간을 업데이트한다.")
    @Test
    void updateTime() {
        // given
        final Meeting meeting = MORAGORA.create();
        final Event event = EVENT1.create(meeting);
        final LocalDateTime beforeStartedTime = LocalDateTime.of(event.getDate(), event.getStartTime())
                .minusMinutes(1);
        final Event eventForUpdate = new Event(event.getDate(), LocalTime.of(11, 5),
                LocalTime.of(12, 5), meeting);

        // when
        event.updateTime(beforeStartedTime, eventForUpdate);

        // then
        assertAll(
                () -> assertThat(event.getStartTime()).isEqualTo(LocalTime.of(11, 5)),
                () -> assertThat(event.getEndTime()).isEqualTo(LocalTime.of(12, 5))
        );
    }

    @DisplayName("이벤트를 업데이트하려고 할 때 이미 이벤트가 시작된 경우 예외가 발생한다.")
    @Test
    void update_throwException_ifEventAlreadyStart() {
        // given
        final Meeting meeting = MORAGORA.create();
        final Event event = EVENT1.create(meeting);
        final LocalDateTime alreadyStartedTime = LocalDateTime.of(event.getDate(), event.getStartTime())
                .plusMinutes(1);
        final Event eventForUpdate = new Event(event.getDate(), LocalTime.of(11, 5),
                LocalTime.of(12, 5), meeting);

        // when, then
        assertThatThrownBy(() -> event.updateTime(alreadyStartedTime, eventForUpdate))
                .isInstanceOf(IllegalAlreadyStartedEventException.class);
    }

    @DisplayName("이벤트가 활성화 되었는지 확인한다.")
    @Test
    void isActive() {
        // given
        final Meeting meeting = MORAGORA.create();
        final Event event = EVENT1.create(meeting);

        final LocalDateTime serverDateTime = LocalDateTime.of(2022, 8, 1, 9, 50);
        final ServerTimeManager serverTimeManager = ServerDateTimeFixtures.createServerTimeManager(serverDateTime);

        // when
        final LocalDate today = LocalDate.of(2022, 8, 1);
        final boolean actual = event.isActive(today, serverTimeManager);

        // then
        assertThat(actual).isEqualTo(true);
    }

    @DisplayName("출석부가 열리는 시간을 조회한다.")
    @Test
    void getOpenTime() {
        // given
        final Meeting meeting = MORAGORA.create();
        final Event event = EVENT1.create(meeting);

        final LocalDateTime serverDateTime = LocalDateTime.of(2022, 8, 1, 9, 50);
        final ServerTimeManager serverTimeManager = ServerDateTimeFixtures.createServerTimeManager(serverDateTime);

        // when
        final LocalTime openTime = event.getOpenTime(serverTimeManager);

        // then
        assertThat(openTime).isEqualTo(LocalTime.of(9, 30));
    }

    @DisplayName("출석부가 닫히는 시간을 조회한다.")
    @Test
    void getCloseTime() {
        // given
        final Meeting meeting = MORAGORA.create();
        final Event event = EVENT1.create(meeting);

        final LocalDateTime serverDateTime = LocalDateTime.of(2022, 8, 1, 9, 50);
        final ServerTimeManager serverTimeManager = ServerDateTimeFixtures.createServerTimeManager(serverDateTime);

        // when
        final LocalTime openTime = event.getCloseTime(serverTimeManager);

        // then
        assertThat(openTime).isEqualTo(LocalTime.of(10, 5));
    }
}
