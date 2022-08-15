package com.woowacourse.moragora.repository;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.EventFixtures.EVENT3;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.AZPI;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.support.DataSupport;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
        final Meeting meeting = MORAGORA.create();
        final Participant participant = dataSupport.saveParticipant(KUN.create(), meeting, false);
        final Event event1 = EVENT1.create(meeting);
        final Event savedEvent = eventRepository.save(event1);

        attendanceRepository.save(new Attendance(Status.TARDY, false, participant, savedEvent));

        final Optional<Event> event = eventRepository.findByMeetingIdAndDate(meeting.getId(), savedEvent.getDate());

        // when
        final Optional<Attendance> attendance = attendanceRepository
                .findByParticipantIdAndEventId(participant.getId(), event.get().getId());

        // then
        assertThat(attendance.isPresent()).isTrue();
    }

    @DisplayName("미팅 참가자들의 특정 날짜 이전의 출석 기록을 조회한다.")
    @Test
    void findByParticipantIdInAndDateLessThanEqual() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();

        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        final Participant participant1 = dataSupport.saveParticipant(user1, meeting);
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);

        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        final Event event3 = dataSupport.saveEvent(EVENT3.create(meeting));

        dataSupport.saveAttendance(participant1, event1, Status.TARDY);
        dataSupport.saveAttendance(participant2, event2, Status.TARDY);
        dataSupport.saveAttendance(participant1, event3, Status.TARDY);

        final List<Participant> participants = List.of(participant1, participant2);
        final List<Long> participantIds = participants.stream()
                .map(Participant::getId)
                .collect(Collectors.toList());

        // when
        final List<Attendance> attendances = attendanceRepository
                .findByParticipantIdInAndDateLessThanEqual(participantIds, LocalDate.of(2022, 8, 2));

        // then
        assertThat(attendances).hasSize(2);
    }

    @DisplayName("미팅 참가자들의 해당 날짜 출석정보 목록을 조회한다.")
    @Test
    void findByParticipantIdInAndEventId() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();

        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        final Participant participant1 = dataSupport.saveParticipant(user1, meeting);
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);

        final Event event = EVENT1.create(meeting);
        final Event savedEvent = eventRepository.save(event);

        final Attendance attendance1 = dataSupport.saveAttendance(participant1, savedEvent, Status.TARDY);

        final List<Participant> participants = List.of(participant1, participant2);

        final List<Long> participantIds = participants.stream()
                .map(Participant::getId)
                .collect(Collectors.toList());

        // when
        final List<Attendance> attendances =
                attendanceRepository.findByParticipantIdInAndEventId(participantIds, attendance1.getEvent().getId());

        // then
        assertThat(attendances).hasSize(1);
    }
}
