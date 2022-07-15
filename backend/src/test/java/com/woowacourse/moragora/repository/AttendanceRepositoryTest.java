package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Attendance;
import java.util.List;
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

    @DisplayName("미팅 참가자의 누적 출석정보를 조회한다")
    @Test
    void findByParticipantId() {
        // given
        final Long participantId = 1L;

        // when
        final List<Attendance> attendances = attendanceRepository.findByParticipantId(participantId);

        // then
        assertThat(attendances.size()).isEqualTo(3);
    }
}
