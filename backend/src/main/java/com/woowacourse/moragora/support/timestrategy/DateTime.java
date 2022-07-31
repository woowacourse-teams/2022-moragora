package com.woowacourse.moragora.support.timestrategy;

import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class DateTime {

    protected LocalDateTime value;

    public DateTime(final LocalDateTime dateTime) {
        this.value = dateTime;
    }

    public LocalDateTime getValue() {
        return value;
    }

    public abstract void changeDateTime(final LocalDateTime dateTime);

    public boolean isBetween(final LocalTime startTime, final LocalTime endTime) {
        final LocalTime time = value.toLocalTime();
        return (time.isAfter(startTime) && time.isBefore(endTime)) ||
                time.equals(startTime);
    }

    public boolean isAfter(final LocalTime endTime) {
        final LocalTime time = value.toLocalTime();
        return time.isAfter(endTime);
    }
}
