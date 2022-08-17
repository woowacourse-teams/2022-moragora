package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Participant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ParticipantRepository extends Repository<Participant, Long> {

    Participant save(final Participant participant);

    List<Participant> findByMeetingId(final Long meetingId);

    Optional<Participant> findByMeetingIdAndUserId(final Long meetingId, final Long userId);

    List<Participant> findByUserId(final Long userId);

    void delete(final Participant participant);
}
