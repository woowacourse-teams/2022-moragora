package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Participant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ParticipantRepositoryTest {

    @Autowired
    private ParticipantRepository participantRepository;
    
    @DisplayName("마팅 Id로 참자가 정보를 조회한다.")
    @Test
    void findByMeetingId() {
        // given
        final Long meetingId = 1L;

        // when
        final List<Participant> participants = participantRepository.findByMeetingId(meetingId);

        // then
        assertThat(participants).hasSize(7);
    }

    @DisplayName("회원 Id와 미팅 Id로 참가자 정보를 조회한다.")
    @Test
    void findByMeetingIdAndUserId() {
        // given
        final Long meetingId = 1L;
        final Long userId = 1L;

        // when
        final Optional<Participant> participant = participantRepository.findByMeetingIdAndUserId(meetingId,
                userId);

        // then
        assertThat(participant.isPresent()).isTrue();
    }
}
