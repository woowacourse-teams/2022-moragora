package com.woowacourse.moragora.domain.participant;

import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.UserFixtures.KUN;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParticipantTest {

    @DisplayName("Meeting과 Participant를 맵핑한다.")
    @Test
    void mapMeeting() {
        // given
        final User user = KUN.create();
        final Meeting meeting = MORAGORA.create();

        final Participant participant = new Participant(user, meeting, false);

        // when
        participant.mapMeeting(meeting);

        // then
        assertThat(meeting.getParticipants()).hasSize(1);
    }

    @DisplayName("Meeting 목록에 이미 Participant가 있으면 포함하지 않는다.")
    @Test
    void mapMeeting_participantList() {
        // given
        final User user = KUN.create();
        final Meeting meeting = MORAGORA.create();

        final Participant participant = new Participant(user, meeting, false);
        participant.mapMeeting(meeting);

        // when
        participant.mapMeeting(meeting);

        // then
        assertThat(meeting.getParticipants()).hasSize(1);
    }
}
