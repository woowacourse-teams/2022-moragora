package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Event;
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

    List<Event> findByMeetingIdAndDateLessThanEqual(final Long meetingId, final LocalDate date);

    Optional<Event> findFirstByMeetingIdAndDateGreaterThanEqualOrderByDate(final Long meetingId, final LocalDate date);

    Optional<Event> findByMeetingIdAndDate(final Long meetingId, final LocalDate date);

    @Query("select e from Event e join fetch e.meeting m join fetch m.participants where e.id=:id")
    Optional<Event> findById(@Param("id") final Long id);

    Long countByMeetingIdAndDateGreaterThanEqual(final Long meetingId, final LocalDate date);

    @Modifying
    @Query("delete from Event e where e.meeting.id = :meetingId and e.date in :dates")
    void deleteByDateInAndMeetingId(@Param("dates") final List<LocalDate> dates,
                                    @Param("meetingId") final Long meetingId);

    List<Event> findByMeetingIdAndDateIn(Long meetingId, List<LocalDate> dates);
}
