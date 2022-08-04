package com.woowacourse.moragora.entity;

import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.FORKY;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.SUN;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.user.EncodedPassword;
import com.woowacourse.moragora.entity.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

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
        final Meeting meeting = new Meeting("미팅1", date, date, time, time);
        final Participant participant1 = new Participant(user1, meeting, true);
        final Participant participant2 = new Participant(user2, meeting, false);
        final Participant participant3 = new Participant(user3, meeting, false);

        final Attendance attendance1 = new Attendance(participant1, date, false, Status.fromEnum(status1));
        final Attendance attendance2 = new Attendance(participant2, date, false, Status.fromEnum(status2));
        final Attendance attendance3 = new Attendance(participant3, date, false, Status.fromEnum(status3));

        final MeetingAttendances meetingAttendances = new MeetingAttendances(
                List.of(attendance1, attendance2, attendance3), 3);

        // when
        final int actual = meetingAttendances.countTardy();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("비활성화할 개수만큼 출석부의 데이터를 비활성화한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void disableAttendances(final int sizeToDisable) {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDate date = now.toLocalDate();

        final User user1 = KUN.create();
        final User user2 = SUN.create();
        final User user3 = FORKY.create();
        final Meeting meeting = MORAGORA.create();
        final Participant participant1 = new Participant(user1, meeting, true);
        final Participant participant2 = new Participant(user2, meeting, false);
        final Participant participant3 = new Participant(user3, meeting, false);

        final Attendance attendance1 = new Attendance(participant1, date, false, Status.TARDY);
        final Attendance attendance2 = new Attendance(participant2, date, false, Status.TARDY);
        final Attendance attendance3 = new Attendance(participant3, date, false, Status.TARDY);

        final List<Attendance> attendances = List.of(attendance1, attendance2, attendance3);
        final MeetingAttendances meetingAttendances = new MeetingAttendances(attendances, 3);

        // when
        meetingAttendances.disableAttendances(sizeToDisable);

        // then
        assertThat(meetingAttendances.countTardy()).isEqualTo(attendances.size() - sizeToDisable);
    }
}
