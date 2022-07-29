package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Attendance;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @DisplayName("미팅 참가자의 해당 날짜 출석정보를 조회한다.")
    @Test
    void findByParticipantIdAndAttendanceDate() {
        // given
        final Long participantId = 1L;
        final LocalDate attendanceDate = LocalDate.of(2022, 7, 14);

        // when
        final Optional<Attendance> attendance = attendanceRepository
                .findByParticipantIdAndAttendanceDate(participantId, attendanceDate);

        // then
        assertThat(attendance.isPresent()).isTrue();
    }
}
