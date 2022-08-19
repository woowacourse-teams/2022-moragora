package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Participant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface ParticipantRepository extends Repository<Participant, Long> {

    Participant save(final Participant participant);

    List<Participant> findByMeetingId(final Long meetingId);

    Optional<Participant> findByMeetingIdAndUserId(final Long meetingId, final Long userId);

    List<Participant> findByUserId(final Long userId);

    void delete(final Participant participant);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Participant p where p.id in :ids")
    void deleteByIdIn(@Param("ids") final List<Long> ids);
}
