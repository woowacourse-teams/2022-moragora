package com.woowacourse.moragora.repository.meeting;

import com.woowacourse.moragora.entity.Meeting;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class MeetingHibernateRepository implements MeetingRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public MeetingHibernateRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Meeting save(final Meeting meeting) {
        entityManager.persist(meeting);
        return meeting;
    }

    public Optional<Meeting> findById(final Long id) {
        final Meeting meeting = entityManager.find(Meeting.class, id);
        return Optional.ofNullable(meeting);
    }
}
