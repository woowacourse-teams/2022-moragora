package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Attendance;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class AttendanceRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public AttendanceRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Attendance save(final Attendance attendance) {
        entityManager.persist(attendance);
        return attendance;
    }

    public Optional<Attendance> findByParticipantIdAndAttendanceDate(final Long participantId,
                                                                     final LocalDate attendanceDate) {
        try {
            final String sql =
                    "select a from Attendance a "
                            + "where a.participant.id = :participantId "
                            + "and a.attendanceDate = :attendanceDate";

            final Attendance attendance = entityManager.createQuery(sql, Attendance.class)
                    .setParameter("participantId", participantId)
                    .setParameter("attendanceDate", attendanceDate)
                    .getSingleResult();
            return Optional.ofNullable(attendance);
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    public List<Attendance> findByParticipantIds(final List<Long> participantIds) {
        return entityManager.createQuery("select a from Attendance a where a.participant.id in :participantIds",
                        Attendance.class)
                .setParameter("participantIds", participantIds)
                .getResultList();
    }

    public List<Attendance> findByParticipantIdsAndAttendanceDate(final List<Long> participantIds,
                                                                  final LocalDate attendanceDate) {
        return entityManager.createQuery("select a from Attendance a "
                                + "where a.participant.id in :participantIds and a.attendanceDate = :attendanceDate",
                        Attendance.class)
                .setParameter("participantIds", participantIds)
                .setParameter("attendanceDate", attendanceDate)
                .getResultList();
    }
}
