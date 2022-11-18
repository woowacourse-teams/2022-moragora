package com.woowacourse.moragora.domain.participant;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface ParticipantRepository extends Repository<Participant, Long> {

    Participant save(final Participant participant);

    @Query("select p from Participant p "
            + " join fetch p.user u "
            + " where p.meeting.id = :meetingId ")
    List<Participant> findByMeetingId(@Param("meetingId") final Long meetingId);

    Optional<Participant> findByMeetingIdAndUserId(final Long meetingId, final Long userId);

    List<Participant> findByUserId(final Long userId);

    @Query("select new com.woowacourse.moragora.domain.participant.TardyCountDto( "
            + " p.id , "
            + " (select count(a) "
            + " from Attendance a "
            + " where a.participant.id = p.id "
            + "   and a.status = 'TARDY' "
            + "   and a.disabled = false )"
            + " ) "
            + " from Participant p "
            + " where p in :participants ")
    List<TardyCountDto> countParticipantsTardy(@Param("participants") final List<Participant> participants);

    void delete(final Participant participant);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Participant p where p.id in :ids")
    void deleteByIdIn(@Param("ids") final List<Long> ids);
}
