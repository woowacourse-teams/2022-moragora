package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Participant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findByMeetingId(final Long meetingId);

    Optional<Participant> findByMeetingIdAndUserId(final Long meetingId, final Long userId);

    List<Participant> findByUserId(final Long userId);
}
