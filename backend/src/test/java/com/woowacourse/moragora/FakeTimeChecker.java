package com.woowacourse.moragora;

import com.woowacourse.moragora.service.closingstrategy.TimeChecker;
import com.woowacourse.moragora.util.CurrentDateTime;
import java.time.LocalTime;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeTimeChecker extends TimeChecker {

    public FakeTimeChecker() {
        super(new CurrentDateTime());
    }

    public FakeTimeChecker(final CurrentDateTime currentDateTime) {
        super(currentDateTime);
    }

    @Override
    public boolean isAttendanceTime(final LocalTime now, final LocalTime entranceTime) {
        return true;
    }

    @Override
    public boolean isExcessClosingTime(final LocalTime now, final LocalTime entranceTime) {
        return false;
    }

    @Override
    public boolean isExcessClosingTime(final LocalTime entranceTime) {
        return false;
    }

    @Override
    public LocalTime calculateOpeningTime(final LocalTime entranceTime) {
        return entranceTime.minusMinutes(30);
    }

    @Override
    public LocalTime calculateClosingTime(final LocalTime entranceTime) {
        return entranceTime.plusMinutes(5);
    }
}
