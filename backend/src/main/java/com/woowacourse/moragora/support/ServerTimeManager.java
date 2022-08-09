package com.woowacourse.moragora.support;

import com.woowacourse.moragora.support.timestrategy.DateTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class ServerTimeManager {

    private static final int ATTENDANCE_END_INTERVAL = 5;
    private static final int ATTENDANCE_START_INTERVAL = 30;

    private final DateTime dateTime;

    public ServerTimeManager(final DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isAttendanceTime(final LocalTime entranceTime) {
        final LocalTime attendanceStartTime = entranceTime.minusMinutes(ATTENDANCE_START_INTERVAL);
        final LocalTime attendanceClosingTime = entranceTime.plusMinutes(ATTENDANCE_END_INTERVAL);

        return dateTime.isBetween(attendanceStartTime, attendanceClosingTime);
    }

    public boolean isOverClosingTime(final LocalTime entranceTime) {
        final LocalTime closingTime = entranceTime.plusMinutes(ATTENDANCE_END_INTERVAL);
        return dateTime.isAfter(closingTime);
    }

    public LocalTime calculateClosingTime(final LocalTime entranceTime) {
        return entranceTime.plusMinutes(ATTENDANCE_END_INTERVAL);
    }

    public LocalTime calculateOpeningTime(final LocalTime entranceTime) {
        return entranceTime.minusMinutes(ATTENDANCE_START_INTERVAL);
    }

    public void refresh(final LocalDateTime localDateTime) {
        dateTime.changeDateTime(localDateTime);
    }

    public LocalDate getDate() {
        return dateTime.getValue().toLocalDate();
    }

    public LocalDateTime getDateAndTime() {
        return dateTime.getValue();
    }
}
