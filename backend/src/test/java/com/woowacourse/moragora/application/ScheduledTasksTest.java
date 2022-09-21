package com.woowacourse.moragora.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.domain.attendance.Attendance;
import com.woowacourse.moragora.domain.attendance.Status;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.support.fixture.MeetingFixtures;
import com.woowacourse.moragora.support.fixture.UserFixtures;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.TaskScheduler;

@SpringBootTest
class ScheduledTasksTest {

    @Autowired
    private ScheduledTasks scheduledTasks;

    @Autowired
    private TaskScheduler taskScheduler;

    @BeforeEach
    void setUp() {
        scheduledTasks.getValues().clear();
    }

    @DisplayName("ScheduledTasks에 스케줄을 추가한다.")
    @Test
    void put() {
        // given
        final User user = UserFixtures.SUN.create();
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Participant participant = new Participant(user, meeting, false);
        final Event event = new Event(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(18, 0), meeting);
        final Attendance attendance = new Attendance(Status.NONE, false, participant, event);
        final ScheduledFuture<?> schedule = taskScheduler.schedule(() -> {
        }, Date.from(Instant.now()));

        // when
        scheduledTasks.put(attendance, schedule);

        // then
        assertThat(scheduledTasks.getValues()).hasSize(1);
    }

    @DisplayName("ScheduledTasks에 스케줄을 제거한다.")
    @Test
    void remove() {
        // given
        final User user = UserFixtures.SUN.create();
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Participant participant = new Participant(user, meeting, false);
        final Event event = new Event(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(18, 0), meeting);
        final Attendance attendance = new Attendance(Status.NONE, false, participant, event);
        final ScheduledFuture<?> schedule = taskScheduler.schedule(() -> {
        }, Date.from(Instant.now()));
        scheduledTasks.put(attendance, schedule);

        // when
        scheduledTasks.remove(attendance);

        // then
        assertThat(scheduledTasks.getValues()).isEmpty();
    }

    @DisplayName("ScheduledTasks의 값을 불러온다.")
    @Test
    void getValues() {
        // given
        final User user = UserFixtures.SUN.create();
        final Meeting meeting = MeetingFixtures.MORAGORA.create();
        final Participant participant = new Participant(user, meeting, false);
        final Event event = new Event(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(18, 0), meeting);
        final Attendance attendance = new Attendance(Status.NONE, false, participant, event);
        final ScheduledFuture<?> schedule = taskScheduler.schedule(() -> {
        }, Date.from(Instant.now()));
        scheduledTasks.put(attendance, schedule);

        // when
        final Map<Attendance, ScheduledFuture<?>> expected = scheduledTasks.getValues();

        // then
        assertThat(expected).containsEntry(attendance, schedule);
    }
}
