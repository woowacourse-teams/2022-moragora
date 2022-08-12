package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Attendance;
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

    List<Attendance> findByParticipantIdInAndEventIdIn(final List<Long> participantIds, final List<Long> eventIds);

    List<Attendance> findByParticipantIdInAndEventId(final List<Long> participantIds, final Long eventId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Attendance a where a.event.id in :eventIds")
    void deleteByEventIdIn(@Param("eventIds") final List<Long> eventIds);
}
