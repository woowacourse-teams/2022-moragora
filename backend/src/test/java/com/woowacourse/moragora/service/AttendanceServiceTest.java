package com.woowacourse.moragora.service;

import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.BATD;
import static com.woowacourse.moragora.support.UserFixtures.FORKY;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.MASTER;
import static com.woowacourse.moragora.support.UserFixtures.WOODY;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.meeting.ClosingTimeExcessException;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import com.woowacourse.moragora.support.ServerTimeManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AttendanceServiceTest {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private ServerTimeManager serverTimeManager;

    @Autowired
    private DataSupport dataSupport;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.execute();
    }

    @DisplayName("사용자들의 출석 여부를 변경한다.")
    @Test
    void updateAttendance() {
        // given
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        final User user = KUN.create();
        final Meeting meeting = MORAGORA.create();
        final Participant participant = dataSupport.saveParticipant(user, meeting, false);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 1);
        serverTimeManager.refresh(dateTime);
        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 1), Status.TARDY, false);

        // when, then
        assertThatCode(() -> attendanceService.updateAttendance(meeting.getId(), user.getId(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("사용자의 출석 여부를 변경하려고 할 때, 미팅방이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifMeetingNotFound() {
        // given
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);
        final User user = KUN.create();
        final Long meetingId = 99L;

        // when, then
        assertThatThrownBy(() -> attendanceService.updateAttendance(meetingId, user.getId(), request))
                .isInstanceOf(MeetingNotFoundException.class);
    }

    @DisplayName("사용자의 출석 여부를 변경하려고 할 때, 미팅 참가자가 존재하지 않는다면 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifParticipantNotFound() {
        // given
        final Meeting meeting = MORAGORA.create();

        final User loginUser = dataSupport.saveUser(MASTER.create());
        final User notUserInMeeting = dataSupport.saveUser(FORKY.create());
        final List<User> users = List.of(KUN.create(), WOODY.create(), BATD.create());
        final List<Long> userIds = dataSupport.saveUsers(users);

        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .entranceTime(meeting.getEntranceTime())
                .leaveTime(meeting.getLeaveTime())
                .userIds(userIds)
                .build();

        final Long meetingId = meetingService.save(meetingRequest, loginUser.getId());

        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 2);
        serverTimeManager.refresh(dateTime);

        // when, then
        assertThatThrownBy(() -> attendanceService.updateAttendance(meetingId, notUserInMeeting.getId(), request))
                .isInstanceOf(ParticipantNotFoundException.class);
    }

    @DisplayName("사용자 출석 제출 시간이 마감 시간을 초과할 경우 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifDeadlineTimeExcess() {
        // given
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        final User user = KUN.create();
        final Meeting meeting = MORAGORA.create();
        final Participant participant = dataSupport.saveParticipant(user, meeting, false);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 6);
        serverTimeManager.refresh(dateTime);

        // when, then
        assertThatThrownBy(() -> attendanceService.updateAttendance(participant.getUser().getId(),
                participant.getMeeting().getId(), request))
                .isInstanceOf(ClosingTimeExcessException.class);
    }

    @DisplayName("사용된 커피스택을 비활성화한다.")
    @Test
    void disableUsedTardy() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 1L;

        final LocalDateTime dateTime1 = LocalDateTime.of(2022, 7, 14, 10, 10);
        serverTimeManager.refresh(dateTime1);
        meetingService.findById(meetingId, loginId);
        final LocalDateTime dateTime2 = LocalDateTime.of(2022, 7, 15, 10, 10);
        serverTimeManager.refresh(dateTime2);
        meetingService.findById(meetingId, loginId);

        // when, then
        assertThatCode(() -> attendanceService.disableUsedTardy(meetingId))
                .doesNotThrowAnyException();
    }
}
