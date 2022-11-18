package com.woowacourse.moragora.domain.attendance;

import static com.woowacourse.moragora.domain.attendance.Status.TARDY;
import static com.woowacourse.moragora.domain.user.Provider.CHECKMATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.domain.user.password.EncodedPassword;
import com.woowacourse.moragora.support.fixture.EventFixtures;
import com.woowacourse.moragora.support.fixture.UserFixtures;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MeetingAttendancesTest {

    @DisplayName("미팅 내에서 현재 활성화된 지각 횟수를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "TARDY,TARDY,TARDY,3",
            "TARDY,TARDY,PRESENT,2",
            "TARDY,PRESENT,PRESENT,1",
            "PRESENT,PRESENT,PRESENT,0"
    })
    void countTardy(final String status1, final String status2, final String status3, final int expected) {
        // given
        final EncodedPassword encodedPassword = EncodedPassword.fromRawValue("qwer1234!");
        final LocalDateTime now = LocalDateTime.now();
        final LocalDate date = now.toLocalDate();
        final LocalTime time = now.toLocalTime();
        final User user1 = new User("sun@gmail.com", encodedPassword, "sun");
        final User user2 = new User("kun@gmail.com", encodedPassword, "kun");
        final User user3 = new User("forki@gmail.com", encodedPassword, "forki");
        final Meeting meeting = new Meeting("미팅1");
        final Event event = new Event(date, time, time, meeting);
        final Participant participant1 = new Participant(user1, meeting, true);
        final Participant participant2 = new Participant(user2, meeting, false);
        final Participant participant3 = new Participant(user3, meeting, false);

        final Attendance attendance1 = new Attendance(Status.fromEnum(status1), false, participant1, event);
        final Attendance attendance2 = new Attendance(Status.fromEnum(status2), false, participant2, event);
        final Attendance attendance3 = new Attendance(Status.fromEnum(status3), false, participant3, event);

        final MeetingAttendances meetingAttendances = new MeetingAttendances(
                List.of(attendance1, attendance2, attendance3), 3);

        // when
        final int actual = meetingAttendances.countTardy();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("커피스택으로 사용할 수 있는 유저별 출석부 데이터 개수를 센다.")
    @Test
    void countUsableAttendancesPerUsers() {
        // given
        final EncodedPassword encodedPassword = EncodedPassword.fromRawValue("qwer1234!");
        final User user1 = new User(1L, "sun@gmail.com", encodedPassword, "sun", CHECKMATE);
        final User user2 = new User(2L, "kun@gmail.com", encodedPassword, "kun", CHECKMATE);
        final User user3 = new User(3L, "forky@gmail.com", encodedPassword, "forky", CHECKMATE);

        final LocalDateTime now = LocalDateTime.now();
        final LocalDate endDate = now.toLocalDate();
        final LocalDate startDate = endDate.minusDays(1);
        final LocalTime time = now.toLocalTime();

        final Meeting meeting = new Meeting("미팅1");
        final Event event1 = new Event(startDate, time, time, meeting);
        final Event event2 = new Event(endDate, time, time, meeting);
        final Participant participant1 = new Participant(user1, meeting, true);
        final Participant participant2 = new Participant(user2, meeting, false);
        final Participant participant3 = new Participant(user3, meeting, false);

        final Attendance attendance1 = new Attendance(TARDY, false, participant1, event1);
        final Attendance attendance2 = new Attendance(TARDY, false, participant2, event1);
        final Attendance attendance3 = new Attendance(Status.PRESENT, false, participant3, event1);
        final Attendance attendance4 = new Attendance(TARDY, false, participant1, event2);
        final Attendance attendance5 = new Attendance(Status.PRESENT, false, participant2, event2);
        final Attendance attendance6 = new Attendance(Status.PRESENT, false, participant3, event2);

        final List<Attendance> attendances = List.of(
                attendance1, attendance2, attendance3, attendance4, attendance5, attendance6);
        final MeetingAttendances meetingAttendances = new MeetingAttendances(attendances, 3);

        final Map<User, Long> expected = Map.of(user1, 2L, user2, 1L);
        // when
        final Map<User, Long> actual = meetingAttendances.countUsableAttendancesPerUsers();

        // then
        assertThat(actual).containsExactlyInAnyOrderEntriesOf(expected);
    }

    @DisplayName("참가자의 수 만큼 출석부의 데이터를 비활성화한다.")
    @Test
    void disableAttendances() {
        // given
        final EncodedPassword encodedPassword = EncodedPassword.fromRawValue("qwer1234!");
        final User user1 = new User("sun@gmail.com", encodedPassword, "sun");
        final User user2 = new User("kun@gmail.com", encodedPassword, "kun");
        final User user3 = new User("forky@gmail.com", encodedPassword, "forky");

        final LocalDateTime now = LocalDateTime.now();
        final LocalDate endDate = now.toLocalDate();
        final LocalDate startDate = endDate.minusDays(1);
        final LocalTime time = now.toLocalTime();
        final Meeting meeting = new Meeting("미팅1");
        final Event event1 = new Event(startDate, time, time, meeting);
        final Event event2 = new Event(endDate, time, time, meeting);

        final Participant participant1 = new Participant(user1, meeting, true);
        final Participant participant2 = new Participant(user2, meeting, false);
        final Participant participant3 = new Participant(user3, meeting, false);

        final Attendance attendance1 = new Attendance(TARDY, false, participant1, event1);
        final Attendance attendance2 = new Attendance(TARDY, false, participant2, event1);
        final Attendance attendance3 = new Attendance(TARDY, false, participant3, event1);
        final Attendance attendance4 = new Attendance(TARDY, false, participant1, event2);
        final Attendance attendance5 = new Attendance(TARDY, false, participant2, event2);
        final Attendance attendance6 = new Attendance(TARDY, false, participant3, event2);

        final List<Attendance> attendances = List.of(
                attendance1, attendance2, attendance3, attendance4, attendance5, attendance6);
        final MeetingAttendances meetingAttendances = new MeetingAttendances(attendances, 3);

        // when
        meetingAttendances.disableAttendances();

        // then
        assertThat(meetingAttendances.countTardy()).isEqualTo(3);
    }

    @DisplayName("MeetingAttendances 객체를 만들 때 Meeting이 2개 이상이면 예외가 발생한다.")
    @Test
    void validateSingleMeeting() {
        // given
        final User user = UserFixtures.KUN.create();
        final Meeting meeting1 = Meeting.builder()
                .id(1L)
                .name("모라고라")
                .build();

        final Meeting meeting2 = Meeting.builder()
                .id(2L)
                .name("f12")
                .build();

        final Participant participant1 = new Participant(user, meeting1, false);
        final Participant participant2 = new Participant(user, meeting2, false);
        final Event event1 = EventFixtures.EVENT1.create(meeting1);
        final Attendance attendance1 = new Attendance(TARDY, false, participant1, event1);
        final Attendance attendance2 = new Attendance(TARDY, false, participant2, event1);

        // when, then
        assertThatThrownBy(() -> new MeetingAttendances(List.of(attendance1, attendance2), 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("지각스택이 쌓였는지를 확인한다.")
    @Test
    void isTardyStackFull() {
        // given
        final EncodedPassword encodedPassword = EncodedPassword.fromRawValue("qwer1234!");
        final LocalDateTime now = LocalDateTime.now();
        final LocalDate date = now.toLocalDate();
        final LocalTime time = now.toLocalTime();
        final User user1 = new User("sun@gmail.com", encodedPassword, "sun");
        final User user2 = new User("kun@gmail.com", encodedPassword, "kun");
        final User user3 = new User("forki@gmail.com", encodedPassword, "forki");
        final Meeting meeting = new Meeting("미팅1");
        final Event event = new Event(date, time, time, meeting);
        final Participant participant1 = new Participant(user1, meeting, true);
        final Participant participant2 = new Participant(user2, meeting, false);
        final Participant participant3 = new Participant(user3, meeting, false);

        final Attendance attendance1 = new Attendance(TARDY, false, participant1, event);
        final Attendance attendance2 = new Attendance(TARDY, false, participant2, event);
        final Attendance attendance3 = new Attendance(TARDY, false, participant3, event);

        final MeetingAttendances meetingAttendances = new MeetingAttendances(List.of(attendance1, attendance2, attendance3), 3);

        // when
        final boolean actual = meetingAttendances.isTardyStackFull();

        // then
        assertThat(actual).isEqualTo(true);
    }
}
