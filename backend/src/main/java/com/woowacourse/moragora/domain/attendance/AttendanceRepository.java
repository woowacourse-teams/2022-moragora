package com.woowacourse.moragora.domain.attendance;

import java.time.LocalDate;
import java.time.LocalTime;
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

    // TODO: participantIds 대신 meetingId를 통한 조회
    @Query("select a from Attendance a join fetch a.event e "
            + "where a.participant.id in :participantIds "
            + "and e.date <= :date ")
    List<Attendance> findByParticipantIdInAndDateLessThanEqual(
            @Param("participantIds") final List<Long> participantIds,
            @Param("date") final LocalDate date);

    List<Attendance> findByEventIdIn(final List<Long> eventIds);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Attendance a set a.status='TARDY' where a.id = :id and a.status = 'NONE'")
    void updateAttendanceToTardy(@Param("id") final Long id);

    // TODO: participantIds 대신 meetingId를 통한 조회
    List<Attendance> findByParticipantIdInAndEventId(final List<Long> participantIds, final Long eventId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Attendance a where a.event.id in :eventIds")
    void deleteByEventIdIn(@Param("eventIds") final List<Long> eventIds);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Attendance a where a.participant.id = :participantId")
    void deleteByParticipantId(@Param("participantId") final Long participantId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Attendance a where a.participant.id in :participantIds")
    void deleteByParticipantIdIn(@Param("participantIds") final List<Long> participantIds);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Attendance a set a.status ='TARDY' where a.status = 'NONE' and a.event in "
            + "(select e from Event e where e.date = :today and e.startTime <= :now)")
    int updateByEventDateTimeAndStatus(final LocalDate today, final LocalTime now);
}
