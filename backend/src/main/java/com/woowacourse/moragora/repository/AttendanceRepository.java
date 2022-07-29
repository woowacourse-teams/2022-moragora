package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Attendance;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface AttendanceRepository extends Repository<Attendance, Long> {

    Attendance save(final Attendance attendance);

    List<Attendance> findByParticipantId(final Long participantId);

    Long countByParticipantId(final Long participantId);

    Optional<Attendance> findByParticipantIdAndAttendanceDate(final Long participantId, final LocalDate attendanceDate);

    List<Attendance> findByParticipantIdInAndAttendanceDate(final List<Long> participantIds,
                                                            final LocalDate attendanceDate);

    List<Attendance> findByParticipantIdAndAttendanceDateNot(final Long participantId, final LocalDate today);
}
