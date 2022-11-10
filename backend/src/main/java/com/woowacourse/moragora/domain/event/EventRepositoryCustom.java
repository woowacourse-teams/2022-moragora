package com.woowacourse.moragora.domain.event;

import java.time.LocalDate;
import java.util.Optional;

public interface EventRepositoryCustom {

    Optional<Event> findUpComingEvent(final Long meetingId, final LocalDate date);
}
