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

    void saveAll(final Iterable<Attendance> attendances);

    Optional<Attendance> findByParticipantIdAndEventId(final Long participantId, final Long eventId);

    // TODO: participantIds 대신 meetingId를 통한 조회
    @Query("select a from Attendance a join fetch a.event e "
            + "where a.participant.id in :participantIds "
            + "and e.date <= :date ")
    List<Attendance> findByParticipantIdInAndDateLessThanEqual(
            @Param("participantIds") final List<Long> participantIds,
            @Param("date") final LocalDate date);

    // TODO: participantIds 대신 meetingId를 통한 조회
    List<Attendance> findByParticipantIdInAndEventId(final List<Long> participantIds, final Long eventId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Attendance a where a.event.id in :eventIds")
    void deleteByEventIdIn(@Param("eventIds") final List<Long> eventIds);
}
