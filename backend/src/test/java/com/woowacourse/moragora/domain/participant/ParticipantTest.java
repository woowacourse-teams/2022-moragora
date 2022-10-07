package com.woowacourse.moragora.domain.participant;

import static com.woowacourse.moragora.domain.user.Provider.GOOGLE;
import static com.woowacourse.moragora.domain.user.password.EncodedPassword.fromRawValue;
import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.UserFixtures.KUN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.domain.exception.BusinessException;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @DisplayName("유저 id로 같은 참가자인지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,true", "2,false"})
    void isSameParticipant(final long userId, final boolean actual) {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = User.builder()
                .id(1L)
                .nickname("sun")
                .email("sun@gmail.com")
                .password(fromRawValue("1234asdf!"))
                .provider(GOOGLE)
                .build();
        final Participant participant = new Participant(user, meeting, true);

        // when
        final boolean expected = participant.isSameParticipant(userId);

        // then
        assertThat(expected).isEqualTo(actual);
    }

    @DisplayName("참가자의 지각 횟수를 할당한다.")
    @Test
    void allocateTardyCount() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = User.builder()
                .id(1L)
                .nickname("sun")
                .email("sun@gmail.com")
                .password(fromRawValue("1234asdf!"))
                .provider(GOOGLE)
                .build();
        final Participant participant = new Participant(user, meeting, true);

        // when
        participant.allocateTardyCount(5L);

        // then
        assertThat(participant.getTardyCount()).isEqualTo(5);
    }

    @DisplayName("참가자의 지각 횟수를 반환한다.")
    @Test
    void getTardyCount() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = User.builder()
                .id(1L)
                .nickname("sun")
                .email("sun@gmail.com")
                .password(fromRawValue("1234asdf!"))
                .provider(GOOGLE)
                .build();
        final Participant participant = new Participant(user, meeting, true);
        participant.allocateTardyCount(5L);

        // when
        final Long tardyCount = participant.getTardyCount();

        // then
        assertThat(tardyCount).isEqualTo(5);
    }

    @DisplayName("참가자의 지각 횟수를 반환한다.")
    @Test
    void getTardyCount_throwsException_ifNotAllocated() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = User.builder()
                .id(1L)
                .nickname("sun")
                .email("sun@gmail.com")
                .password(fromRawValue("1234asdf!"))
                .provider(GOOGLE)
                .build();
        final Participant participant = new Participant(user, meeting, true);

        // when, then
        assertThatThrownBy(participant::getTardyCount)
                .isInstanceOf(BusinessException.class)
                .hasMessage("지각 횟수가 할당되지 않았습니다.");
    }
}
