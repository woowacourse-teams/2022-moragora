package com.woowacourse.moragora.support.timestrategy;

import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class DateTime {

    protected LocalDateTime value;

    protected DateTime(final LocalDateTime dateTime) {
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

    public boolean isAfter(final LocalTime other) {
        final LocalTime localTime = value.toLocalTime();
        return localTime.isAfter(other);
    }
}
