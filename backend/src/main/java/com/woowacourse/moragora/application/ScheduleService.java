package com.woowacourse.moragora.application;

import static com.woowacourse.moragora.domain.attendance.Status.TARDY;

import com.woowacourse.moragora.domain.attendance.Attendance;
import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import java.time.LocalDateTime;
import java.util.List;
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
        final List<Attendance> attendancesToUpdate = attendanceRepository
                .findByEventDateTimeAndStatus(now.toLocalDate(), now.toLocalTime());
        attendancesToUpdate.forEach(attendance -> attendance.changeAttendanceStatus(TARDY));
    }
}
