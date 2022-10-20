package com.woowacourse.moragora.domain.attendance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.support.fixture.EventFixtures;
import com.woowacourse.moragora.support.fixture.MeetingFixtures;
import com.woowacourse.moragora.support.fixture.UserFixtures;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParticipantAttendancesTest {

    @DisplayName("ParticipantAttendances 객체를 만들 때 참가자가 두명이상이라면 예외가 발생한다.")
    @Test
    void validateSingleParticipant() {

        // given
        final User user1 = UserFixtures.KUN.create();
        final User user2 = UserFixtures.AZPI.create();
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Participant participant1 = new Participant(1L, user1, meeting, false);
        final Participant participant2 = new Participant(2L, user2, meeting, false);
        final Event event1 = EventFixtures.EVENT1.create(meeting);
        final Attendance attendance1 = new Attendance(Status.TARDY, false, participant1, event1);
        final Attendance attendance2 = new Attendance(Status.TARDY, false, participant2, event1);

        // when, then
        assertThatThrownBy(() -> new ParticipantAttendances(List.of(attendance1, attendance2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Participant의 지각횟수를 카운트한다.")
    @Test
    void countTardy() {
        // given
        final User user = UserFixtures.KUN.create();
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Participant participant = new Participant(user, meeting, false);
        final Event event1 = EventFixtures.EVENT1.create(meeting);
        final Event event2 = EventFixtures.EVENT2.create(meeting);

        final List<Attendance> attendances = List.of(new Attendance(Status.TARDY, false, participant, event1),
                new Attendance(Status.TARDY, false, participant, event2));

        final ParticipantAttendances participantAttendances = new ParticipantAttendances(attendances);

        // when
        final int actual = participantAttendances.countTardy();

        // then
        assertThat(actual).isEqualTo(2);
    }
}
