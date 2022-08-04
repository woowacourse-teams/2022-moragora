package com.woowacourse.moragora.repository;

import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.entity.Event;
import java.time.LocalDate;
import java.util.List;
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

    @Autowired
    private EventRepository eventRepository;

    @DisplayName("미팅 참가자의 해당 날짜 출석정보를 조회한다.")
    @Test
    void findByParticipantIdAndEventId() {
        // given
        final Participant participant = dataSupport.saveParticipant(KUN.create(), MORAGORA.create(), false);
        final LocalDate attendanceDate = LocalDate.of(2022, 7, 14);
        dataSupport.saveAttendance(participant, attendanceDate, Status.TARDY);
        final Optional<Event> event = eventRepository.findByMeetingIdAndDate(1L, attendanceDate);
        assert (event.isPresent());

        // when
        final Optional<Attendance> attendance = attendanceRepository
                .findByParticipantIdAndEventId(participant.getId(), event.get().getId());

        // then
        assertThat(attendance.isPresent()).isTrue();
    }

    @DisplayName("미팅 참가자들의 출석정보 목록을 조회한다.")
    @Test
    void findByParticipantIdIn() {
        // given
        final List<Long> participantIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

        // when
        final List<Attendance> attendances = attendanceRepository.findByParticipantIdIn(participantIds);

        // then
        assertThat(attendances).hasSize(21);
    }

    @DisplayName("미팅 참가자들의 해당 날짜 출석정보 목록을 조회한다.")
    @Test
    void findByParticipantIdInAndEventId() {
        // given
        final List<Long> participantIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        final LocalDate attendanceDate = LocalDate.of(2022, 7, 14);
        final Optional<Event> event = eventRepository.findByMeetingIdAndDate(1L, attendanceDate);
        assert (event.isPresent());

        // when
        final List<Attendance> attendances =
                attendanceRepository.findByParticipantIdInAndEventId(participantIds, event.get().getId());

        // then
        assertThat(attendances).hasSize(7);
    }
}
