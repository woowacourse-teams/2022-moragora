package com.woowacourse.moragora.application;

import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.UserFixtures.SUN;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.domain.attendance.Attendance;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.attendance.Status;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SchedulerTest {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private DataSupport dataSupport;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.execute();
    }

    @DisplayName("출석 시간이 지난 Attendance를 NONE에서 TARDY로 변경한다.")
    @Test
    void updateToTardyAfterClosedTime() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final User user = dataSupport.saveUser(SUN.create());
        final Participant participant = dataSupport.saveParticipant(user, meeting);
        final Event event = dataSupport.saveEvent(EVENT1.create(meeting));
        final Attendance attendance = dataSupport.saveAttendance(participant, event, Status.NONE);

        // when
        scheduler.updateToTardyAfterClosedTime(attendance);
        final Optional<Attendance> expected = dataSupport.findAttendanceByParticipantAndEvent(participant, event);
        assert (expected).isPresent();

        // then
        assertThat(expected.get().isTardy()).isTrue();
    }
}
