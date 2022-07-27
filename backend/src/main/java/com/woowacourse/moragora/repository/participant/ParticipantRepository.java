package com.woowacourse.moragora.repository.participant;

import com.woowacourse.moragora.entity.Participant;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {

    Participant save(final Participant participant);

    List<Participant> findByMeetingId(final Long meetingId);

    Optional<Participant> findByMeetingIdAndUserId(final Long meetingId, final Long userId);

    List<Participant> findByUserId(final Long userId);
}
