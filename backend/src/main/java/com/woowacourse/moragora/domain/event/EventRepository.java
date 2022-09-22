package com.woowacourse.moragora.domain.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends Repository<Event, Long> {

    Event save(final Event event);

    List<Event> saveAll(final Iterable<Event> events);

    long countByMeetingIdAndDateLessThanEqual(final Long meetingId, final LocalDate date);

    Optional<Event> findFirstByMeetingIdAndDateGreaterThanEqualOrderByDate(final Long meetingId, final LocalDate date);

    List<Event> findByMeetingId(final Long meetingId);

    Optional<Event> findByMeetingIdAndDate(final Long meetingId, final LocalDate date);

    @Query("select e from Event e join fetch e.meeting m join fetch m.participants where e.id=:id")
    Optional<Event> findById(@Param("id") final Long id);

    List<Event> findByMeetingIdAndDateIn(final Long meetingId, List<LocalDate> dates);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Event e where e.id in :ids")
    void deleteByIdIn(@Param("ids") final List<Long> ids);

    @Query("select e from Event e join fetch e.meeting m"
            + " where m.id = :meetingId"
            + " and (:begin is null or e.date >= :begin)"
            + " and (:end is null or e.date <= :end)")
    List<Event> findByMeetingIdAndDuration(@Param("meetingId") final Long meetingId,
                                           @Param("begin") final LocalDate begin,
                                           @Param("end") final LocalDate end);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Event e where e.meeting.id = :meetingId")
    void deleteByMeetingId(@Param("meetingId") final Long meetingId);
}
