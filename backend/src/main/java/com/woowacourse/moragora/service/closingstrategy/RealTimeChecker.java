package com.woowacourse.moragora.service.closingstrategy;

import com.woowacourse.moragora.util.CurrentDateTime;
import java.time.LocalTime;

public class RealTimeChecker extends TimeChecker {

    private static final int ATTENDANCE_END_INTERVAL = 5;
    private static final int ATTENDANCE_START_INTERVAL = 30;

    public RealTimeChecker(final CurrentDateTime currentDateTime) {
        super(currentDateTime);
    }

    @Override
    public boolean isAttendanceTime(final LocalTime now, final LocalTime entranceTime) {
        final LocalTime attendanceStartTime = entranceTime.minusMinutes(ATTENDANCE_START_INTERVAL);
        final LocalTime attendanceClosingTime = entranceTime.plusMinutes(ATTENDANCE_END_INTERVAL);

        return (now.isAfter(attendanceStartTime) && now.isBefore(attendanceClosingTime)) ||
                now.equals(attendanceStartTime);
    }

    @Override
    public boolean isExcessClosingTime(final LocalTime entranceTime) {
        final LocalTime now = currentDateTime.getValue().toLocalTime();
        final LocalTime closingTime = entranceTime.plusMinutes(ATTENDANCE_END_INTERVAL);
        return now.isAfter(closingTime);
    }

    @Override
    public boolean isExcessClosingTime(final LocalTime now, final LocalTime entranceTime) {
        final LocalTime closingTime = entranceTime.plusMinutes(ATTENDANCE_END_INTERVAL);
        return now.isAfter(closingTime);
    }

    @Override
    public LocalTime calculateClosingTime(final LocalTime entranceTime) {
        return entranceTime.plusMinutes(ATTENDANCE_END_INTERVAL);
    }
}
