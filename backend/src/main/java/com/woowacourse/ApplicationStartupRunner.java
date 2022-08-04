package com.woowacourse;

import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.entity.user.EncodedPassword;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.repository.AttendanceRepository;
import com.woowacourse.moragora.repository.EventRepository;
import com.woowacourse.moragora.repository.MeetingRepository;
import com.woowacourse.moragora.repository.ParticipantRepository;
import com.woowacourse.moragora.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements ApplicationListener<ContextRefreshedEvent> {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    public ApplicationStartupRunner(final MeetingRepository meetingRepository,
                                    final UserRepository userRepository,
                                    final AttendanceRepository attendanceRepository,
                                    final ParticipantRepository participantRepository,
                                    final EventRepository eventRepository) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        final User user1 = new User("aaa111@foo.com", EncodedPassword.fromRawValue("1234smart!"), "아스피");
        final User user2 = new User("bbb222@foo.com", EncodedPassword.fromRawValue("1234smart!"), "필즈");
        final User user3 = new User("ccc333@foo.com", EncodedPassword.fromRawValue("1234smart!"), "포키");
        final User user4 = new User("ddd444@foo.com", EncodedPassword.fromRawValue("1234smart!"), "썬");
        final User user5 = new User("eee555@foo.com", EncodedPassword.fromRawValue("1234smart!"), "우디");
        final User user6 = new User("fff666@foo.com", EncodedPassword.fromRawValue("1234smart!"), "쿤");
        final User user7 = new User("ggg777@foo.com", EncodedPassword.fromRawValue("1234smart!"), "반듯");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);
        userRepository.save(user7);

        final Meeting meeting = new Meeting("모임1");
        meetingRepository.save(meeting);

        final Participant participant1 = new Participant(user1, meeting, true);
        final Participant participant2 = new Participant(user2, meeting, false);
        final Participant participant3 = new Participant(user3, meeting, false);
        final Participant participant4 = new Participant(user4, meeting, false);
        final Participant participant5 = new Participant(user5, meeting, false);
        final Participant participant6 = new Participant(user6, meeting, false);
        final Participant participant7 = new Participant(user7, meeting, false);

        participantRepository.save(participant1);
        participantRepository.save(participant2);
        participantRepository.save(participant3);
        participantRepository.save(participant4);
        participantRepository.save(participant5);
        participantRepository.save(participant6);
        participantRepository.save(participant7);

        final LocalTime entranceTime = LocalTime.of(10, 0);
        final LocalTime leaveTime = LocalTime.of(18, 0);
        final Event event1 = new Event(LocalDate.of(2022, 7, 12), entranceTime, leaveTime, meeting);
        final Event event2 = new Event(LocalDate.of(2022, 7, 13), entranceTime, leaveTime, meeting);
        final Event event3 = new Event(LocalDate.of(2022, 7, 14), entranceTime, leaveTime, meeting);
        final Event event4 = new Event(LocalDate.of(2022, 7, 15), entranceTime, leaveTime, meeting);
        final Event event5 = new Event(LocalDate.of(2022, 7, 16), entranceTime, leaveTime, meeting);

        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
        eventRepository.save(event4);
        eventRepository.save(event5);

        final Attendance attendance1 = new Attendance(Status.TARDY, false, participant1, event1);
        final Attendance attendance2 = new Attendance(Status.PRESENT, false, participant2, event1);
        final Attendance attendance3 = new Attendance(Status.PRESENT, false, participant3, event1);
        final Attendance attendance4 = new Attendance(Status.PRESENT, false, participant4, event1);
        final Attendance attendance5 = new Attendance(Status.PRESENT, false, participant5, event1);
        final Attendance attendance6 = new Attendance(Status.PRESENT, false, participant6, event1);
        final Attendance attendance7 = new Attendance(Status.PRESENT, false, participant7, event1);

        final Attendance attendance8 = new Attendance(Status.PRESENT, false, participant1, event2);
        final Attendance attendance9 = new Attendance(Status.TARDY, false, participant2, event2);
        final Attendance attendance10 = new Attendance(Status.PRESENT, false, participant3, event2);
        final Attendance attendance11 = new Attendance(Status.PRESENT, false, participant4, event2);
        final Attendance attendance12 = new Attendance(Status.PRESENT, false, participant5, event2);
        final Attendance attendance13 = new Attendance(Status.PRESENT, false, participant6, event2);
        final Attendance attendance14 = new Attendance(Status.PRESENT, false, participant7, event2);

        final Attendance attendance15 = new Attendance(Status.PRESENT, false, participant1, event3);
        final Attendance attendance16 = new Attendance(Status.TARDY, false, participant2, event3);
        final Attendance attendance17 = new Attendance(Status.PRESENT, false, participant3, event3);
        final Attendance attendance18 = new Attendance(Status.PRESENT, false, participant4, event3);
        final Attendance attendance19 = new Attendance(Status.PRESENT, false, participant5, event3);
        final Attendance attendance20 = new Attendance(Status.PRESENT, false, participant6, event3);
        final Attendance attendance21 = new Attendance(Status.PRESENT, false, participant7, event3);

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);
        attendanceRepository.save(attendance3);
        attendanceRepository.save(attendance4);
        attendanceRepository.save(attendance5);
        attendanceRepository.save(attendance6);
        attendanceRepository.save(attendance7);

        attendanceRepository.save(attendance8);
        attendanceRepository.save(attendance9);
        attendanceRepository.save(attendance10);
        attendanceRepository.save(attendance11);
        attendanceRepository.save(attendance12);
        attendanceRepository.save(attendance13);
        attendanceRepository.save(attendance14);

        attendanceRepository.save(attendance15);
        attendanceRepository.save(attendance16);
        attendanceRepository.save(attendance17);
        attendanceRepository.save(attendance18);
        attendanceRepository.save(attendance19);
        attendanceRepository.save(attendance20);
        attendanceRepository.save(attendance21);
    }
}
