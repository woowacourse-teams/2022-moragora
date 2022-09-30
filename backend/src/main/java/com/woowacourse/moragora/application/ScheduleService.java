package com.woowacourse.moragora.application;

import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    private final AttendanceRepository attendanceRepository;

    public ScheduleService(final AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Scheduled(cron = "5 */5 * * * *")
    @Transactional
    public void updateToTardyAtAttendanceClosingTime() {
        final LocalDateTime now = LocalDateTime.now();
        attendanceRepository.updateByEventDateTimeAndStatus(now.toLocalDate(), now.toLocalTime());
        log.info("출석 마감 처리 완료");
    }
}
