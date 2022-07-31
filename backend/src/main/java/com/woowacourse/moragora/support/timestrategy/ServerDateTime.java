package com.woowacourse.moragora.support.timestrategy;

import java.time.LocalDateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class ServerDateTime extends DateTime {

    public ServerDateTime() {
        super(LocalDateTime.now());
    }

    @Override
    public void changeDateTime(final LocalDateTime dateTime) {
        throw new IllegalStateException("서버시간을 변경할 수 없습니다.");
    }
}
