package com.woowacourse.moragora.domain.meeting;

import static com.woowacourse.moragora.domain.user.Provider.GOOGLE;
import static com.woowacourse.moragora.domain.user.password.EncodedPassword.fromRawValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.ParticipantAndCount;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.exception.global.InvalidFormatException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MeetingTest {

    @DisplayName("미팅을 생성한다.")
    @Test
    void createMeeting() {
        // given, when, then
        assertThatCode(() -> new Meeting("모임")).doesNotThrowAnyException();
    }

    @DisplayName("미팅을 생성할 때, 이름이 50자를 초과하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"012345678901234567890123456789012345678901234567891",
            "영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영",
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija"})
    void createMeeting_throwsException_ifNameOver50(final String name) {
        // given, when, then
        assertThatThrownBy(() -> new Meeting(name))
                .isInstanceOf(InvalidFormatException.class);
    }

    @DisplayName("미팅의 이름을 수정한다.")
    @Test
    void updateName() {
        // given
        final Meeting meeting = new Meeting("모임");

        // when, then
        assertThatCode(() -> meeting.updateName("새로운 이름"))
                .doesNotThrowAnyException();
    }

    @DisplayName("50자를 초과하는 이름으로 수정하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"012345678901234567890123456789012345678901234567891",
            "영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영",
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija"})
    void updateName_throwsException_ifNameOver50(final String name) {
        // given
        final Meeting meeting = new Meeting("모임");

        // when, then
        assertThatThrownBy(() -> meeting.updateName(name))
                .isInstanceOf(InvalidFormatException.class);
    }

    @DisplayName("userId로 참가자를 찾는다.")
    @Test
    void findParticipant() {
        // given
        final Meeting meeting = new Meeting(1L, "체크메이트");
        final User user = new User(1L, "sun@gmail.com", fromRawValue("asdf1234!"), "sun", GOOGLE);
        final Participant participant = new Participant(user, meeting, true);
        participant.mapMeeting(meeting);

        // when
        final Participant foundParticipant = meeting.findParticipant(1L).get();

        // then
        assertThat(foundParticipant).isEqualTo(participant);
    }

    @DisplayName("참가자들의 결석 횟수를 할당한다.")
    @Test
    void allocateParticipantsTardyCount() {
        // given
        final Meeting meeting = new Meeting(1L, "체크메이트");
        final User user = new User(1L, "sun@gmail.com", fromRawValue("asdf1234!"), "sun", GOOGLE);
        final Participant participant = new Participant(user, meeting, true);
        ParticipantAndCount participantAndCount = new ParticipantAndCount() {
            @Override
            public Participant getParticipant() {
                return participant;
            }

            @Override
            public Long getTardyCount() {
                return 5L;
            }
        };

        // when
        meeting.allocateParticipantsTardyCount(List.of(participantAndCount));

        // then
        assertThat(participant.getTardyCount()).isEqualTo(5);
    }

    @DisplayName("모임의 지각 횟수가 다 채워졌다면 True를 반환한다.")
    @Test
    void isTardyStackFull() {
        // given
        final Meeting meeting = new Meeting(1L, "체크메이트");
        final User user1 = new User(1L, "sun@gmail.com", fromRawValue("asdf1234!"), "sun", GOOGLE);
        final User user2 = new User(2L, "kun@gmail.com", fromRawValue("asdf1234!"), "kun", GOOGLE);
        final Participant participant1 = new Participant(user1, meeting, true);
        final Participant participant2 = new Participant(user2, meeting, false);
        participant1.mapMeeting(meeting);
        participant2.mapMeeting(meeting);
        participant1.allocateTardyCount(1L);
        participant2.allocateTardyCount(1L);

        // when
        final Boolean isTardyStackFull = meeting.isTardyStackFull();

        // then
        assertThat(isTardyStackFull).isTrue();
    }

    @DisplayName("모임의 지각 횟수가 다 채워지지 않았다면 False를 반환한다.")
    @Test
    void isTardyStackFull_returnsFalse_tardyStackLessThanParticipantsSize() {
        // given
        final Meeting meeting = new Meeting(1L, "체크메이트");
        final User user1 = new User(1L, "sun@gmail.com", fromRawValue("asdf1234!"), "sun", GOOGLE);
        final User user2 = new User(2L, "kun@gmail.com", fromRawValue("asdf1234!"), "kun", GOOGLE);
        final Participant participant1 = new Participant(user1, meeting, true);
        final Participant participant2 = new Participant(user2, meeting, false);
        participant1.mapMeeting(meeting);
        participant2.mapMeeting(meeting);
        participant1.allocateTardyCount(1L);
        participant2.allocateTardyCount(0L);

        // when
        final Boolean isTardyStackFull = meeting.isTardyStackFull();

        // then
        assertThat(isTardyStackFull).isFalse();
    }

    @DisplayName("모임 참가자들의 지각 횟수의 총 합을 계산한다.")
    @Test
    void calculateTardy() {
        // given
        final Meeting meeting = new Meeting(1L, "체크메이트");
        final User user1 = new User(1L, "sun@gmail.com", fromRawValue("asdf1234!"), "sun", GOOGLE);
        final User user2 = new User(2L, "kun@gmail.com", fromRawValue("asdf1234!"), "kun", GOOGLE);
        final Participant participant1 = new Participant(user1, meeting, true);
        final Participant participant2 = new Participant(user2, meeting, false);
        participant1.mapMeeting(meeting);
        participant2.mapMeeting(meeting);
        participant1.allocateTardyCount(5L);
        participant2.allocateTardyCount(3L);

        // when
        final long totalTardyCount = meeting.calculateTardy();

        // then
        assertThat(totalTardyCount).isEqualTo(8);
    }
}
