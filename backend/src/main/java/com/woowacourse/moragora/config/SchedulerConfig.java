package com.woowacourse.moragora.config;

import com.woowacourse.moragora.application.AttendanceScheduler;
import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {

    private final AttendanceRepository attendanceRepository;
    private final boolean isScheduler;
    private final ServerTimeManager serverTimeManager;

    public SchedulerConfig(@Value("${jvm.args.scheduler}") final boolean isScheduler,
                           final AttendanceRepository attendanceRepository,
                           final ServerTimeManager serverTimeManager) {
        this.isScheduler = isScheduler;
        this.attendanceRepository = attendanceRepository;
        this.serverTimeManager = serverTimeManager;
    }

    @Bean
    public AttendanceScheduler scheduleService() {
        if (isScheduler) {
            return new AttendanceScheduler(attendanceRepository, serverTimeManager);
        }

        return null;
    }
}
