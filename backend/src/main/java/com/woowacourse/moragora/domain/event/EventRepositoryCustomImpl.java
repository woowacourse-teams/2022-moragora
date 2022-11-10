package com.woowacourse.moragora.domain.event;

import java.time.LocalDate;
import java.util.Optional;
import javax.persistence.EntityManager;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final EntityManager entityManager;

    public EventRepositoryCustomImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<Event> findUpComingEvent(final Long meetingId, final LocalDate date) {

        final String sql = "select e from Event e left join e.meeting m where m.id = :meetingId and e.date >= :date order by e.date asc";

        final Event event = entityManager.createQuery(
                        sql,
                        Event.class)
                .setParameter("meetingId", meetingId)
                .setParameter("date", date)
                .setMaxResults(1)
                .getSingleResult();

        return Optional.ofNullable(event);
    }
}
