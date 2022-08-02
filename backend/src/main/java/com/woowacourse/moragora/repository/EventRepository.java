package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Event;
import org.springframework.data.repository.Repository;

public interface EventRepository extends Repository<Event, Long> {

    Event save(Event event);
}
