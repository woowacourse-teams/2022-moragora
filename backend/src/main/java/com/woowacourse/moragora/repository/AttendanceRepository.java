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

    List<Attendance> saveAll(final Iterable<Attendance> attendances);

    Optional<Attendance> findByParticipantIdAndEventId(final Long participantId, final Long eventId);

    List<Attendance> findByParticipantIdIn(final List<Long> participantIds);

    List<Attendance> findByParticipantIdInAndEventId(List<Long> participantIds, Long eventId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Attendance a set a.status='TARDY' where a.id = :id and a.status = 'NONE'")
    void updateAttendanceToTardy(@Param("id") final Long id);
}
