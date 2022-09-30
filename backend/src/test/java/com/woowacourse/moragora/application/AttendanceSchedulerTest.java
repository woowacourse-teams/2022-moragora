package com.woowacourse.moragora.application;

import static com.woowacourse.moragora.domain.attendance.Status.NONE;
import static com.woowacourse.moragora.domain.attendance.Status.TARDY;
import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT_WITHOUT_DATE;
import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.UserFixtures.KUN;
import static com.woowacourse.moragora.support.fixture.UserFixtures.PHILLZ;
import static com.woowacourse.moragora.support.fixture.UserFixtures.SUN;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.domain.attendance.Attendance;
import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class AttendanceSchedulerTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AttendanceScheduler attendanceScheduler;

    @Autowired
    private DataSupport dataSupport;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.execute();
    }

    @DisplayName("같은 날짜에 출석시간이 지난 event들이 있다면, 해당 event의 attendance들을 TARDY로 변경시킨다.")
    @Test
    void updateToTardyAtAttendanceClosingTime() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final User user1 = dataSupport.saveUser(SUN.create());
        final User user2 = dataSupport.saveUser(KUN.create());
        final User user3 = dataSupport.saveUser(PHILLZ.create());
        final Event event = EVENT_WITHOUT_DATE
                .createEventOnDateAndTime(meeting, LocalDate.now(), LocalTime.now());
        final Event savedEvent = dataSupport.saveEvent(event);
        final Participant participant1 = dataSupport.saveParticipant(user1, meeting, true);
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);
        dataSupport.saveAttendance(participant1, savedEvent, NONE);
        dataSupport.saveAttendance(participant2, savedEvent, NONE);
        dataSupport.saveAttendance(participant3, savedEvent, NONE);

        // when
        attendanceScheduler.updateToTardyAtAttendanceClosingTime();

        // then
        assertThat(attendanceRepository.findByEventIdIn(List.of(savedEvent.getId())))
                .extracting(Attendance::getStatus)
                .isEqualTo(List.of(TARDY, TARDY, TARDY));
    }

    @DisplayName("같은 날짜가 아니라면 event의 attendance들을 변경시키지 않는다.")
    @Test
    void updateToTardyAtAttendanceClosingTime_doesNotUpdate_ifDateDiffer() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final User user1 = dataSupport.saveUser(SUN.create());
        final User user2 = dataSupport.saveUser(KUN.create());
        final User user3 = dataSupport.saveUser(PHILLZ.create());
        final Event event = EVENT_WITHOUT_DATE
                .createEventOnDateAndTime(meeting, LocalDate.now().minusDays(1), LocalTime.now());
        final Event savedEvent = dataSupport.saveEvent(event);
        final Participant participant1 = dataSupport.saveParticipant(user1, meeting, true);
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);
        final Attendance attendance1 = dataSupport.saveAttendance(participant1, savedEvent, NONE);
        final Attendance attendance2 = dataSupport.saveAttendance(participant2, savedEvent, NONE);
        final Attendance attendance3 = dataSupport.saveAttendance(participant3, savedEvent, NONE);

        // when
        attendanceScheduler.updateToTardyAtAttendanceClosingTime();

        // then
        assertThat(List.of(attendance1, attendance2, attendance3))
                .extracting(Attendance::getStatus)
                .isEqualTo(List.of(NONE, NONE, NONE));
    }

    @DisplayName("같은 날짜지만, 아직 출석중인 event라면 attendance들을 변경시키지 않는다.")
    @Test
    void updateToTardyAtAttendanceClosingTime_doesNotUpdate_ifBeforeAttendanceCloseTime() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final User user1 = dataSupport.saveUser(SUN.create());
        final User user2 = dataSupport.saveUser(KUN.create());
        final User user3 = dataSupport.saveUser(PHILLZ.create());
        final Event event = EVENT_WITHOUT_DATE
                .createEventOnDateAndTime(meeting, LocalDate.now(), LocalTime.now().plusMinutes(1));
        final Event savedEvent = dataSupport.saveEvent(event);
        final Participant participant1 = dataSupport.saveParticipant(user1, meeting, true);
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);
        final Attendance attendance1 = dataSupport.saveAttendance(participant1, savedEvent, NONE);
        final Attendance attendance2 = dataSupport.saveAttendance(participant2, savedEvent, NONE);
        final Attendance attendance3 = dataSupport.saveAttendance(participant3, savedEvent, NONE);

        // when
        attendanceScheduler.updateToTardyAtAttendanceClosingTime();

        assertThat(List.of(attendance1, attendance2, attendance3))
                .extracting(Attendance::getStatus)
                .isEqualTo(List.of(NONE, NONE, NONE));
    }
}
