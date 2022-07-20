package com.woowacourse.moragora.service.closingstrategy;

import java.time.LocalTime;

public interface ServerTime {

    boolean isAttendanceTime(final LocalTime now, final LocalTime entranceTime);

    boolean isExcessClosingTime(final LocalTime now, final LocalTime entranceTime);

    LocalTime calculateClosingTime(LocalTime entranceTime);
}
