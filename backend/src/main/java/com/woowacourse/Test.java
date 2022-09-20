package com.woowacourse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Test {

    // 0 * * * * MON-FRI
    @Scheduled(cron = "0/2 * * * * MON-FRI")
    public void allocate() {
        log.info("Test.allocate: " + Thread.activeCount());
    }
}
