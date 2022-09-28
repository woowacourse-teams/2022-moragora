package com.woowacourse.moragora.application;

import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT3;
import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.UserFixtures.AZPI;
import static com.woowacourse.moragora.support.fixture.UserFixtures.FORKY;
import static com.woowacourse.moragora.support.fixture.UserFixtures.KUN;
import static com.woowacourse.moragora.support.fixture.UserFixtures.PHILLZ;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.domain.attendance.Attendance;
import com.woowacourse.moragora.domain.attendance.Status;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.dto.request.user.UserAttendanceRequest;
import com.woowacourse.moragora.dto.response.attendance.AttendanceResponse;
import com.woowacourse.moragora.dto.response.attendance.AttendancesResponse;
import com.woowacourse.moragora.dto.response.meeting.CoffeeStatResponse;
import com.woowacourse.moragora.dto.response.meeting.CoffeeStatsResponse;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.event.EventNotFoundException;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.exception.meeting.NotCheckInTimeException;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AttendanceServiceTest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ServerTimeManager serverTimeManager;

    @Autowired
    private DataSupport dataSupport;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();
    }

    @DisplayName("사용자들의 출석 여부를 변경한다.")
    @Test
    void updateAttendance() {
        // given
        final User user = KUN.create();
        final Meeting meeting = MORAGORA.create();
        final Participant participant = dataSupport.saveParticipant(user, meeting, false);
        final Event event = dataSupport.saveEvent(EVENT1.create(meeting));

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 1);
        serverTimeManager.refresh(dateTime);
        dataSupport.saveAttendance(participant, event, Status.TARDY);

        final UserAttendanceRequest request = new UserAttendanceRequest(true);

        // when, then
        assertThatCode(() -> attendanceService.updateAttendance(meeting.getId(), user.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("사용자의 출석 여부를 변경하려고 할 때, 미팅방이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifMeetingNotFound() {
        // given
        final User user = KUN.create();
        final Long meetingId = 99L;

        final UserAttendanceRequest request = new UserAttendanceRequest(true);

        // when, then
        assertThatThrownBy(() -> attendanceService.updateAttendance(meetingId, user.getId(), request))
                .isInstanceOf(MeetingNotFoundException.class);
    }

    @DisplayName("사용자의 출석 여부를 변경하려고 할 때, 미팅 참가자가 존재하지 않는다면 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifParticipantNotFound() {
        // given
        final User user1 = dataSupport.saveUser(KUN.create());
        final User user2 = dataSupport.saveUser(AZPI.create());
        final User user3 = dataSupport.saveUser(PHILLZ.create());
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        dataSupport.saveParticipant(user1, meeting, true);
        dataSupport.saveParticipant(user2, meeting);

        final UserAttendanceRequest request = new UserAttendanceRequest(true);

        // when, then
        assertThatThrownBy(() -> attendanceService.updateAttendance(meeting.getId(), user3.getId(), request))
                .isInstanceOf(ParticipantNotFoundException.class);
    }

    @DisplayName("사용자의 출석 여부를 변경하려고 할 때, 당일 일정이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifEventNotFound() {
        // given
        final User user = KUN.create();
        final Meeting meeting = MORAGORA.create();
        final Participant participant = dataSupport.saveParticipant(user, meeting, false);
        dataSupport.saveEvent(EVENT1.create(meeting));

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 2, 10, 0);
        serverTimeManager.refresh(dateTime);

        final UserAttendanceRequest request = new UserAttendanceRequest(true);

        // when, then
        assertThatThrownBy(() -> attendanceService.updateAttendance(participant.getUser().getId(),
                participant.getMeeting().getId(), request))
                .isInstanceOf(EventNotFoundException.class);
    }

    @DisplayName("출석 제출 시간이 출석부 활성화 시간이 아닐 경우 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource({"9,29", "10,6"})
    void updateAttendance_throwsException_ifDeadlineTimeExcess(int hour, int minute) {
        // given
        final User user = KUN.create();
        final Meeting meeting = MORAGORA.create();
        final Participant participant = dataSupport.saveParticipant(user, meeting, false);
        dataSupport.saveEvent(EVENT1.create(meeting));

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, hour, minute);
        serverTimeManager.refresh(dateTime);

        final UserAttendanceRequest request = new UserAttendanceRequest(true);

        // when, then
        assertThatThrownBy(() -> attendanceService.updateAttendance(participant.getUser().getId(),
                participant.getMeeting().getId(), request))
                .isInstanceOf(NotCheckInTimeException.class);
    }

    @DisplayName("유저별 다음에 사용될 커피스택을 조회한다.")
    @Test
    void countUsableCoffeeStack() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final User user1 = dataSupport.saveUser(AZPI.create());
        final User user2 = dataSupport.saveUser(PHILLZ.create());
        final User user3 = dataSupport.saveUser(FORKY.create());

        final Participant participant1 = dataSupport.saveParticipant(user1, meeting);
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);

        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        final Event event3 = dataSupport.saveEvent(EVENT3.create(meeting));

        dataSupport.saveAttendance(participant1, event1, Status.PRESENT);
        dataSupport.saveAttendance(participant1, event2, Status.TARDY);
        dataSupport.saveAttendance(participant1, event3, Status.PRESENT);

        dataSupport.saveAttendance(participant2, event1, Status.TARDY);
        dataSupport.saveAttendance(participant2, event2, Status.TARDY);
        dataSupport.saveAttendance(participant2, event3, Status.PRESENT);

        dataSupport.saveAttendance(participant3, event1, Status.PRESENT);
        dataSupport.saveAttendance(participant3, event2, Status.PRESENT);
        dataSupport.saveAttendance(participant3, event3, Status.TARDY);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 4, 10, 6);
        serverTimeManager.refresh(dateTime);

        // when
        final CoffeeStatsResponse response = attendanceService.countUsableCoffeeStack(meeting.getId());

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(new CoffeeStatsResponse(
                        List.of(
                                new CoffeeStatResponse(user1.getId(), user1.getNickname(), 1L),
                                new CoffeeStatResponse(user2.getId(), user2.getNickname(), 2L)
                        ))
                );
    }

    @DisplayName("사용된 커피스택을 비활성화한다.")
    @Test
    void disableUsedTardy() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = KUN.create();
        final Participant participant = dataSupport.saveParticipant(user, meeting);
        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveAttendance(participant, event1, Status.TARDY);
        dataSupport.saveAttendance(participant, event2, Status.TARDY);

        // when, then
        assertThatCode(() -> attendanceService.disableUsedTardy(meeting.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("오늘의 출석부를 조회한다.")
    @Test
    void findTodayAttendanceByMeeting() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = KUN.create();
        final Participant participant = dataSupport.saveParticipant(user, meeting);
        final Event event = dataSupport.saveEvent(EVENT1.create(meeting));
        final Attendance attendance = dataSupport.saveAttendance(participant, event, Status.NONE);
        serverTimeManager.refresh(EVENT1.getDate().atTime(EVENT1.getEntranceTime()));

        // when
        final AttendancesResponse attendancesResponse = attendanceService.findTodayAttendancesByMeeting(
                meeting.getId());

        // then
        assertThat(attendancesResponse).usingRecursiveComparison()
                .isEqualTo(new AttendancesResponse(List.of(AttendanceResponse.from(attendance))));
    }


    @DisplayName("오늘의 일정이 존재하지 않을 경우 출석부를 조회하면 예외가 발생한다.")
    @Test
    void findTodayAttendanceByMeeting_throwsException_ifEventNotExists() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = KUN.create();
        final Participant participant = dataSupport.saveParticipant(user, meeting);
        final Event event = dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveAttendance(participant, event, Status.NONE);

        // when, then
        assertThatCode(() -> attendanceService.findTodayAttendancesByMeeting(meeting.getId()))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("오늘의 일정이 존재하지 않아 출석부를 조회할 수 없습니다.");
    }
}
