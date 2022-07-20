package com.woowacourse.moragora;

import com.woowacourse.moragora.service.closingstrategy.TimeChecker;
import com.woowacourse.moragora.util.CurrentDateTime;
import java.time.LocalTime;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeTimeChecker extends TimeChecker {

    public FakeTimeChecker(final CurrentDateTime currentDateTime) {
        super(currentDateTime);
    }

    @Override
    public boolean isExcessClosingTime(final LocalTime entranceTime) {
        return false;
    }
}
