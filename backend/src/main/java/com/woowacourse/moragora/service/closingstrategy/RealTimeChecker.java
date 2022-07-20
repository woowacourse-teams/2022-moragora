package com.woowacourse.moragora.service.closingstrategy;

import com.woowacourse.moragora.util.CurrentDateTime;
import java.time.LocalTime;

public class RealTimeChecker extends TimeChecker {

    private static final int ATTENDANCE_CHECK_DURATION = 5;

    public RealTimeChecker(final CurrentDateTime currentDateTime) {
        super(currentDateTime);
    }

    @Override
    public boolean isExcessClosingTime(final LocalTime entranceTime) {
        final LocalTime now = currentDateTime.getValue().toLocalTime();
        final LocalTime closingTime = entranceTime.plusMinutes(ATTENDANCE_CHECK_DURATION);
        return now.isAfter(closingTime);
    }
}
