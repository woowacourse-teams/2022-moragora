package com.woowacourse.moragora.config;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig extends ThreadPoolTaskScheduler {

    private static final long serialVersionUID = 1L;

    @Override
    public ScheduledFuture<?> schedule(final Runnable task, final Date startTime) {
        return super.schedule(task, startTime);
    }
}
