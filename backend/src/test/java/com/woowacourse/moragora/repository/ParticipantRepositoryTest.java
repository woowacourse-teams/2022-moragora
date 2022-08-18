package com.woowacourse.moragora.repository;

import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.SUN;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.support.DataSupport;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(DataSupport.class)
@DataJpaTest
class ParticipantRepositoryTest {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private DataSupport dataSupport;

    @DisplayName("미팅 Id로 참자가 정보를 조회한다.")
    @Test
    void findByMeetingId() {
        // given
        final User user = KUN.create();
        final Meeting meeting = MORAGORA.create();
        dataSupport.saveParticipant(user, meeting, false);
        dataSupport.saveParticipant(SUN.create(), meeting, false);

        // when
        final List<Participant> participants = participantRepository.findByMeetingId(meeting.getId());

        // then
        assertThat(participants).hasSize(2);
    }

    @DisplayName("회원 Id와 미팅 Id로 참가자 정보를 조회한다.")
    @Test
    void findByMeetingIdAndUserId() {
        // given
        final User user = KUN.create();
        final Meeting meeting = MORAGORA.create();
        dataSupport.saveParticipant(user, meeting, false);

        // when
        final Optional<Participant> participant = participantRepository.findByMeetingIdAndUserId(meeting.getId(),
                user.getId());

        // then
        assertThat(participant.isPresent()).isTrue();
    }

    @DisplayName("participant id들로 참가자 정보를 삭제한다.")
    @Test
    void deleteByIdIn() {
        // given
        final User user = KUN.create();
        final Meeting meeting = MORAGORA.create();
        final Participant participant = dataSupport.saveParticipant(user, meeting, false);

        // when
        participantRepository.deleteByIdIn(List.of(participant.getId()));

        // then
        final List<Participant> participants = participantRepository.findByMeetingId(meeting.getId());
        assertThat(participants.size()).isEqualTo(0);
    }
}
