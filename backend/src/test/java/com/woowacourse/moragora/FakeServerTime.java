package com.woowacourse.moragora;

import com.woowacourse.moragora.service.closingstrategy.ServerTime;
import java.time.LocalTime;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeServerTime implements ServerTime {
    @Override
    public boolean isExcessClosingTime(final LocalTime now, final LocalTime entranceTime) {
        return false;
    }
}
