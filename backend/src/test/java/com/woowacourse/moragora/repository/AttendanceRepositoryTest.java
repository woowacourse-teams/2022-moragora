package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Participant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    @Autowired
    private ParticipantRepository participantRepository;

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

    @DisplayName("미팅 참가자의 출결일 수 를 조회한다.")
    @Test
    void countByParticipantId() {
        // given
        final Long participantId = 1L;

        // when
        final Long attendanceCount = attendanceRepository.countByParticipantId(participantId);

        // then
        assertThat(attendanceCount).isEqualTo(3);
    }

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

    @DisplayName("미팅 참가자들의 특정 날짜 출석들을 조회한다.")
    @Test
    void findByParticipantIdInAndAttendanceDate() {
        // given
        final Long meetingId = 1L;
        final LocalDate attendanceDate = LocalDate.of(2022, 7, 14);
        final List<Participant> participants = participantRepository.findByMeetingId(meetingId);
        final List<Long> participantIds = participants.stream()
                .map(Participant::getId)
                .collect(Collectors.toList());

        // when
        final List<Attendance> attendances = attendanceRepository
                .findByParticipantIdInAndAttendanceDate(participantIds, attendanceDate);

        // then
        assertThat(attendances).hasSize(participantIds.size());
    }

    @DisplayName("특정일을 제외한 미팅 참가자의 출결일수를 조회한다.")
    @Test
    void findByParticipantIdAndAttendanceDateNot() {
        // given
        final Long participantId = 1L;
        final LocalDate attendanceDate = LocalDate.of(2022, 7, 14);

        // when
        final List<Attendance> attendances = attendanceRepository
                .findByParticipantIdAndAttendanceDateNot(participantId, attendanceDate);
        final List<LocalDate> attendanceDates = attendances.stream()
                .map(Attendance::getAttendanceDate)
                .collect(Collectors.toList());

        // then
        assertThat(attendances).hasSize(2);
        assertThat(attendanceDates.contains(attendanceDate)).isFalse();
    }
}
