package com.woowacourse.moragora.service.closingstrategy;

import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class RealServerTime implements ServerTime {

    private static final int EXTRA_TIME = 5;

    @Override
    public boolean isExcessClosingTime(final LocalTime now, final LocalTime entranceTime) {
        final LocalTime closingTime = entranceTime.plusMinutes(EXTRA_TIME);
        return now.isAfter(closingTime);
    }
}
