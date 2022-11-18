package com.woowacourse.moragora.support.fixture;

import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.support.timestrategy.DateTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ServerDateTimeFixtures {

    public static ServerTimeManager createServerTimeManager(final LocalDateTime localDateTime) {

        final DateTime dateTime = getConcreteDateTime(localDateTime);

        return new ServerTimeManager(dateTime);
    }

    public static ServerTimeManager createServerTimeManager(final LocalDate localDate) {

        return createServerTimeManager(LocalDateTime.of(localDate, LocalTime.of(0, 0)));
    }

    private static DateTime getConcreteDateTime(final LocalDateTime localDateTime) {
        return new DateTime(localDateTime) {
            @Override
            public void changeDateTime(final LocalDateTime dateTime) {
            }
        };
    }
}
