package com.woowacourse.moragora.application;

import com.woowacourse.moragora.domain.attendance.Attendance;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final Map<Attendance, ScheduledFuture<?>> values;

    private ScheduledTasks() {
        values = new ConcurrentHashMap<>();
    }

    public void put(final Attendance attendance, final ScheduledFuture<?> schedule) {
        values.put(attendance, schedule);
    }

    public void remove(final Attendance attendance) {
        values.remove(attendance);
    }

    public Map<Attendance, ScheduledFuture<?>> getValues() {
        return values;
    }
}
