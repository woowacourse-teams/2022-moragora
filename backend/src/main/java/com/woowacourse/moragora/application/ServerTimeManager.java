package com.woowacourse.moragora.application;

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

    public boolean isAttendanceOpen(final LocalTime meetingStartTime) {
        final LocalTime attendanceOpenTime = meetingStartTime.minusMinutes(ATTENDANCE_START_INTERVAL);
        final LocalTime attendanceClosedTime = meetingStartTime.plusMinutes(ATTENDANCE_END_INTERVAL);

        return dateTime.isBetween(attendanceOpenTime, attendanceClosedTime);
    }

    public boolean isAttendanceClosed(final LocalTime meetingStartTime) {
        final LocalTime closingTime = meetingStartTime.plusMinutes(ATTENDANCE_END_INTERVAL);
        return dateTime.isAfter(closingTime);
    }

    public boolean isAfter(final LocalTime startTime) {
        return dateTime.isAfter(startTime);
    }

    public LocalTime calculateOpenTime(final LocalTime meetingStartTime) {
        return meetingStartTime.minusMinutes(ATTENDANCE_START_INTERVAL);
    }

    public LocalTime calculateAttendanceCloseTime(final LocalTime meetingStartTime) {
        return meetingStartTime.plusMinutes(ATTENDANCE_END_INTERVAL);
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
