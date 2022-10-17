package com.woowacourse.moragora.config;

import com.woowacourse.moragora.application.AttendanceScheduler;
import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {

    private final AttendanceRepository attendanceRepository;
    private final boolean isScheduler;

    public SchedulerConfig(final AttendanceRepository attendanceRepository,
                           @Value("${jvm.args.scheduler}") final boolean isScheduler) {
        this.attendanceRepository = attendanceRepository;
        this.isScheduler = isScheduler;
    }

    @Bean
    public AttendanceScheduler scheduleService() {
        if (isScheduler) {
            return new AttendanceScheduler(attendanceRepository);
        }

        return null;
    }
}
