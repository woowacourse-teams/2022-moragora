package com.woowacourse.moragora.service.closingstrategy;

import com.woowacourse.moragora.util.CurrentDateTime;
import java.time.LocalTime;

public abstract class TimeChecker {

    protected CurrentDateTime currentDateTime;

    public TimeChecker(final CurrentDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public abstract boolean isAttendanceTime(final LocalTime now, final LocalTime entranceTime);

    public abstract boolean isExcessClosingTime(final LocalTime entranceTime);

    public abstract boolean isExcessClosingTime(final LocalTime now, final LocalTime entranceTime);

    public abstract LocalTime calculateOpeningTime(final LocalTime entranceTime);

    public abstract LocalTime calculateClosingTime(final LocalTime entranceTime);
}
