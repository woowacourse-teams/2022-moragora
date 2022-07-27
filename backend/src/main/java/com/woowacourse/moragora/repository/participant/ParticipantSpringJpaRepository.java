package com.woowacourse.moragora.repository.participant;

import com.woowacourse.moragora.entity.Participant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ParticipantSpringJpaRepository extends JpaRepository<Participant, Long>, ParticipantRepository {

    @Transactional
    Participant save(final Participant participant);

    List<Participant> findByMeetingId(final Long meetingId);

    Optional<Participant> findByMeetingIdAndUserId(final Long meetingId, final Long userId);

    List<Participant> findByUserId(final Long userId);
}
