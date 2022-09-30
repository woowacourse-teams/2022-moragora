package com.woowacourse.moragora.application;

import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private final AttendanceRepository attendanceRepository;

    public ScheduleService(final AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Scheduled(cron = "5 */5 * * * *")
    @Transactional
    public void updateToTardyAtAttendanceClosingTime() {
        final LocalDateTime now = LocalDateTime.now();
        attendanceRepository.updateByEventDateTimeAndStatus(now.toLocalDate(), now.toLocalTime());
    }
}
