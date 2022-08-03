package com.woowacourse.moragora.repository;

import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.support.DataSupport;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(DataSupport.class)
@DataJpaTest
class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private DataSupport dataSupport;

    @DisplayName("미팅 참가자의 해당 날짜 출석정보를 조회한다.")
    @Test
    void findByParticipantIdAndAttendanceDate() {
        // given
        final Participant participant = dataSupport.saveParticipant(KUN.create(), MORAGORA.create(), false);
        final LocalDate attendanceDate = LocalDate.of(2022, 7, 14);
        dataSupport.saveAttendance(participant, attendanceDate, Status.TARDY, false);

        // when
        final Optional<Attendance> attendance = attendanceRepository
                .findByParticipantIdAndAttendanceDate(participant.getId(), attendanceDate);

        // then
        assertThat(attendance.isPresent()).isTrue();
    }
}
