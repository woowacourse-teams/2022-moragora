package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Participant;
import java.time.LocalDate;
import java.util.List;
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

    @DisplayName("미팅 참가자의 해당 날짜 출석정보를 조회한다.")
    @Test
    void findByParticipantIdAndDate() {
        // given
        final Long participantId = 1L;
        final LocalDate attendanceDate = LocalDate.now();

        // when
        final Optional<Attendance> attendance = attendanceRepository.findByParticipantIdAndAttendanceDate(
                participantId,
                attendanceDate);

        // then
        assertThat(attendance.isPresent()).isTrue();
    }

    @DisplayName("미팅 참가자 Id와 미팅 Id로 출석 정보를 조회한다.")
    @Test
    void findByMeetingIdAndUserId() {
        // given
        final Long meetingId = 1L;
        final Long userId = 1L;

        // when
        final Optional<Participant> participant = attendanceRepository.findByMeetingIdAndUserId(meetingId,
                userId);

        // then
        assertThat(participant.isPresent()).isTrue();
    }
}
