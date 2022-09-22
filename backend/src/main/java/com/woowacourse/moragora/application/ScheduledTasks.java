package com.woowacourse.moragora.application;

import com.woowacourse.moragora.domain.event.Event;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final Map<Event, ScheduledFuture<?>> values;

    private ScheduledTasks() {
        values = new ConcurrentHashMap<>();
    }

    public void put(final Event event, final ScheduledFuture<?> schedule) {
        values.put(event, schedule);
    }

    public void remove(final Event event) {
        values.remove(event);
    }

    public Map<Event, ScheduledFuture<?>> getValues() {
        return values;
    }
}
