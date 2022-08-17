package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Attendance;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepository extends Repository<Attendance, Long> {

    Attendance save(final Attendance attendance);

    List<Attendance> saveAll(final Iterable<Attendance> attendances);

    Optional<Attendance> findByParticipantIdAndEventId(final Long participantId, final Long eventId);

    List<Attendance> findByParticipantIdIn(final List<Long> participantIds);

    List<Attendance> findByEventIdIn(final List<Long> eventIds);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Attendance a set a.status='TARDY' where a.id = :id and a.status = 'NONE'")
    void updateAttendanceToTardy(@Param("id") final Long id);

    List<Attendance> findByParticipantIdInAndEventId(final List<Long> participantIds, final Long eventId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Attendance a where a.event.id in :eventIds")
    void deleteByEventIdIn(@Param("eventIds") final List<Long> eventIds);

    @Query("select a from Attendance a where a.participant.meeting.id = :meetingId and a.event.date in :eventDates")
    List<Attendance> findByMeetingIdAndDateIn(@Param("meetingId") final Long meetingId,
                                              @Param("eventDates") final List<LocalDate> eventDates);
}
