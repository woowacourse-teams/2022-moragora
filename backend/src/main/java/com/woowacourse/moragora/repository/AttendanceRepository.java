package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Participant;
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

    public List<Attendance> findByParticipantId(final Long participantId) {
        return entityManager.createQuery("select a from Attendance a where a.participant.id = :participantId",
                Attendance.class)
                .setParameter("participantId", participantId)
                .getResultList();
    }

    public Optional<Attendance> findByParticipantIdAndAttendanceDate(final Long participantId,
                                                                     final LocalDate attendanceDate) {
        try {
            final Attendance attendance = entityManager.createQuery(
                    "select a from Attendance a where a.participant.id = :participantId and a.attendanceDate = :attendanceDate",
                    Attendance.class)
                    .setParameter("participantId", participantId)
                    .setParameter("attendanceDate", attendanceDate)
                    .getSingleResult();
            return Optional.ofNullable(attendance);
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    public Optional<Participant> findByMeetingIdAndUserId(final Long meetingId, final Long userId) {
        try {
            final Participant participant = entityManager.createQuery(
                    "select p from Participant p where p.meeting.id = :meetingId "
                            + "and p.user.id = :userId",
                    Participant.class)
                    .setParameter("meetingId", meetingId)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.ofNullable(participant);
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    public List<Attendance> findByParticipantIdAndAttendanceDateNot(final Long participantId, final LocalDate today) {
        return entityManager.createQuery("select a from Attendance a where a.participant.id = :participantId "
                + "and a.attendanceDate <> :today", Attendance.class)
                .setParameter("participantId", participantId)
                .setParameter("today", today)
                .getResultList();
    }
}
