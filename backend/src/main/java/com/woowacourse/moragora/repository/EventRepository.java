package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Event;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface EventRepository extends Repository<Event, Long> {

    Event save(final Event event);

    List<Event> findByMeetingIdAndDateLessThanEqual(final Long meetingId, final LocalDate date);

    Optional<Event> findFirstByMeetingIdAndDateGreaterThanEqualOrderByDate(final Long meetingId, final LocalDate date);

    Optional<Event> findByMeetingIdAndDate(final Long meetingId, final LocalDate date);

    Long countByMeetingIdAndDateGreaterThanEqual(final Long meetingId, final LocalDate date);
}
