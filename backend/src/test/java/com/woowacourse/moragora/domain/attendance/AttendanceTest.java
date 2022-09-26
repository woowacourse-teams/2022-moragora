package com.woowacourse.moragora.domain.attendance;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.support.fixture.EventFixtures;
import com.woowacourse.moragora.support.fixture.MeetingFixtures;
import com.woowacourse.moragora.support.fixture.UserFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AttendanceTest {

    @DisplayName("Attendance 상태가 변경되는지 확인한다.")
    @Test
    void changeAttendanceStatus() {
        // given
        final User user = UserFixtures.KUN.create();
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Event event = EventFixtures.EVENT1.create(meeting);
        final Participant participant = new Participant(user, meeting, false);
        final Attendance attendance = new Attendance(Status.NONE, false, participant, event);

        // when
        attendance.changeAttendanceStatus(Status.TARDY);

        // then
        assertThat(attendance.getStatus()).isEqualTo(Status.TARDY);
    }

    @DisplayName("Attendance가 비활성화 되는지 확인한다.")
    @Test
    void disable() {
        // given
        final User user = UserFixtures.KUN.create();
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Event event = EventFixtures.EVENT1.create(meeting);
        final Participant participant = new Participant(user, meeting, false);
        final Attendance attendance = new Attendance(Status.NONE, false, participant, event);

        // when
        attendance.disable();

        // then
        assertThat(attendance.getDisabled()).isTrue();
    }

    @DisplayName("Attendance 상태가 Tardy인지 확인한다.")
    @Test
    void isTardy() {
        // given
        final User user = UserFixtures.KUN.create();
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Event event = EventFixtures.EVENT1.create(meeting);
        final Participant participant = new Participant(user, meeting, false);
        final Attendance attendance = new Attendance(Status.NONE, false, participant, event);

        // when, then
        assertThat(attendance.isTardy()).isFalse();
    }

    @DisplayName("Attendance 상태가 None 인지 확인한다.")
    @Test
    void isNone() {
        // given
        final User user = UserFixtures.KUN.create();
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Event event = EventFixtures.EVENT1.create(meeting);
        final Participant participant = new Participant(user, meeting, false);
        final Attendance attendance = new Attendance(Status.NONE, false, participant, event);

        // when, then
        assertThat(attendance.isNone()).isTrue();
    }

    @DisplayName("Attendance가 활성화 되있는지 확인한다.")
    @Test
    void isEnabled() {
        // given
        final User user = UserFixtures.KUN.create();
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Event event = EventFixtures.EVENT1.create(meeting);
        final Participant participant = new Participant(user, meeting, false);
        final Attendance attendance = new Attendance(Status.NONE, false, participant, event);

        // when, then
        assertThat(attendance.isEnabled()).isTrue();
    }
}
