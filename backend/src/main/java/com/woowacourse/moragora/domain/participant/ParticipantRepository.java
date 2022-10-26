package com.woowacourse.moragora.domain.participant;

import java.time.LocalDate;
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

    @Query("select new com.woowacourse.moragora.domain.participant.Participant( "
            + " p , u, "
            + " (select count(a) "
            + " from Attendance a "
            + " where a.participant.id = p.id and a.status = 'TARDY' and a.disabled = false )) "
            + " from Participant p "
            + " inner join p.user u"
            + " where p in :participants")
    List<Participant> countParticipantsTardy(@Param("participants") final List<Participant> participants);

    void delete(final Participant participant);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Participant p where p.id in :ids")
    void deleteByIdIn(@Param("ids") final List<Long> ids);
}
