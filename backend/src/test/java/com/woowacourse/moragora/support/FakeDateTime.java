package com.woowacourse.moragora.support;

import com.woowacourse.moragora.support.timestrategy.DateTime;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Primary
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FakeDateTime extends DateTime {

    public FakeDateTime() {
        super(LocalDateTime.now());
    }

    @Override
    public void changeDateTime(final LocalDateTime dateTime) {
        super.value = dateTime;
    }
}
