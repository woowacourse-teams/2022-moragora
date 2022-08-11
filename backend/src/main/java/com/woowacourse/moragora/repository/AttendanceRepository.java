package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Attendance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface AttendanceRepository extends Repository<Attendance, Long> {

    Attendance save(final Attendance attendance);

    void saveAll(final Iterable<Attendance> attendances);

    Optional<Attendance> findByParticipantIdAndEventId(final Long participantId, final Long eventId);

    List<Attendance> findByParticipantIdIn(final List<Long> participantIds);

    List<Attendance> findByParticipantIdInAndEventId(final List<Long> participantIds, final Long eventId);

    void deleteByEventIdIn(final List<Long> eventIds);
}
