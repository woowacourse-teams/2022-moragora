package com.woowacourse.moragora.domain.participant;

import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.UserFixtures.AZPI;
import static com.woowacourse.moragora.support.fixture.UserFixtures.KUN;
import static com.woowacourse.moragora.support.fixture.UserFixtures.SUN;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.user.User;
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

    @DisplayName("id로 여러 참가자를 삭제한다.")
    @Test
    void deleteByIdIn() {
        // given
        final User user1 = KUN.create();
        final User user2 = SUN.create();
        final User user3 = AZPI.create();
        final Meeting meeting = MORAGORA.create();
        final Participant participant1 = dataSupport.saveParticipant(user1, meeting, false);
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting, false);
        dataSupport.saveParticipant(user3, meeting, false);

        // when
        participantRepository.deleteByIdIn(List.of(participant1.getId(), participant2.getId()));
        final List<Participant> result = participantRepository.findByMeetingId(meeting.getId());

        // then
        assertThat(result).hasSize(1);
    }

}
