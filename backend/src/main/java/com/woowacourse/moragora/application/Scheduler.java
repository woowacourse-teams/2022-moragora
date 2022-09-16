package com.woowacourse.moragora.application;

import com.woowacourse.moragora.domain.attendance.Attendance;
import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduler 비동기 호출시 트랜잭션 적용을 위해 분리
 */
@Component
public class Scheduler {

    private final AttendanceRepository attendanceRepository;

    public Scheduler(final AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Transactional
    public void updateToTardyAfterClosedTime(final Attendance attendance) {
        attendanceRepository.updateAttendanceToTardy(attendance.getId());
    }
}
