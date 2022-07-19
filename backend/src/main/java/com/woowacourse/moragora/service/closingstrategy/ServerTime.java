package com.woowacourse.moragora.service.closingstrategy;

import java.time.LocalTime;

public interface ServerTime {

    boolean isExcessClosingTime(final LocalTime now, final LocalTime entranceTime);
}
