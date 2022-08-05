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
        final User user8 = new User("user1@google.com", EncodedPassword.fromRawValue("user1pw!"), "우영우");
        final User user9 = new User("user2@google.com", EncodedPassword.fromRawValue("user2pw!"), "한가인");
        final User user10 = new User("user3@google.com", EncodedPassword.fromRawValue("user3pw!"), "이종석");
        final User user11 = new User("user4@google.com", EncodedPassword.fromRawValue("user4pw!"), "주우재");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);
        userRepository.save(user7);
        userRepository.save(user8);
        userRepository.save(user9);
        userRepository.save(user10);
        userRepository.save(user11);

        final Meeting meeting1 = new Meeting("모임1");
        final Meeting meeting2 = new Meeting("알고리즘 뽀개기");
        final Meeting meeting3 = new Meeting("연기 피드백");
        final Meeting meeting4 = new Meeting("대본 리딩");

        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);
        meetingRepository.save(meeting3);
        meetingRepository.save(meeting4);

        final Participant participant1 = new Participant(user1, meeting1, true);
        final Participant participant2 = new Participant(user2, meeting1, false);
        final Participant participant3 = new Participant(user3, meeting1, false);
        final Participant participant4 = new Participant(user4, meeting1, false);
        final Participant participant5 = new Participant(user5, meeting1, false);
        final Participant participant6 = new Participant(user6, meeting1, false);
        final Participant participant7 = new Participant(user7, meeting1, false);

        final Participant participant8 = new Participant(user8, meeting2, true);
        final Participant participant9 = new Participant(user9, meeting2, false);
        final Participant participant10 = new Participant(user10, meeting2, false);
        final Participant participant11 = new Participant(user11, meeting2, false);

        final Participant participant12 = new Participant(user8, meeting3, true);
        final Participant participant13 = new Participant(user9, meeting3, false);
        final Participant participant14 = new Participant(user10, meeting3, false);
        final Participant participant15 = new Participant(user11, meeting3, false);


        final Participant participant16 = new Participant(user8, meeting4, true);
        final Participant participant17 = new Participant(user9, meeting4, false);
        final Participant participant18 = new Participant(user10, meeting4, false);
        final Participant participant19 = new Participant(user11, meeting4, false);

        participantRepository.save(participant1);
        participantRepository.save(participant2);
        participantRepository.save(participant3);
        participantRepository.save(participant4);
        participantRepository.save(participant5);
        participantRepository.save(participant6);
        participantRepository.save(participant7);
        participantRepository.save(participant8);
        participantRepository.save(participant9);
        participantRepository.save(participant10);
        participantRepository.save(participant11);
        participantRepository.save(participant12);
        participantRepository.save(participant13);
        participantRepository.save(participant14);
        participantRepository.save(participant15);
        participantRepository.save(participant16);
        participantRepository.save(participant17);
        participantRepository.save(participant18);
        participantRepository.save(participant19);

        final LocalTime entranceTime = LocalTime.of(10, 0);
        final LocalTime leaveTime = LocalTime.of(18, 0);
        final Event event1 = new Event(LocalDate.of(2022, 7, 12), entranceTime, leaveTime, meeting1);
        final Event event2 = new Event(LocalDate.of(2022, 7, 13), entranceTime, leaveTime, meeting1);
        final Event event3 = new Event(LocalDate.of(2022, 7, 14), entranceTime, leaveTime, meeting1);
        final Event event4 = new Event(LocalDate.of(2022, 7, 15), entranceTime, leaveTime, meeting1);
        final Event event5 = new Event(LocalDate.of(2022, 7, 16), entranceTime, leaveTime, meeting1);

        //meeting3
        final Event event6 = new Event(LocalDate.of(2022, 7, 1), entranceTime, leaveTime, meeting3);
        final Event event7 = new Event(LocalDate.of(2022, 7, 2), entranceTime, leaveTime, meeting3);
        final Event event8 = new Event(LocalDate.of(2022, 9, 2), entranceTime, leaveTime, meeting3);

        //meeting4
        final Event event9 = new Event(LocalDate.of(2022, 7, 4), entranceTime, leaveTime, meeting4);
        final Event event10 = new Event(LocalDate.of(2022, 7, 5), entranceTime, leaveTime, meeting4);
        final Event event11 = new Event(LocalDate.of(2022, 9, 3), entranceTime, leaveTime, meeting4);

        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
        eventRepository.save(event4);
        eventRepository.save(event5);
        eventRepository.save(event6);
        eventRepository.save(event7);
        eventRepository.save(event8);
        eventRepository.save(event9);
        eventRepository.save(event10);
        eventRepository.save(event11);

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

        // meeting 3
        final Attendance attendance22 = new Attendance(Status.PRESENT, false, participant12, event6);
        final Attendance attendance23 = new Attendance(Status.TARDY, false, participant13, event6);
        final Attendance attendance24 = new Attendance(Status.PRESENT, false, participant14, event6);
        final Attendance attendance25 = new Attendance(Status.PRESENT, false, participant15, event6);

        final Attendance attendance26 = new Attendance(Status.TARDY, false, participant12, event7);
        final Attendance attendance27 = new Attendance(Status.TARDY, false, participant13, event7);
        final Attendance attendance28 = new Attendance(Status.PRESENT, false, participant14, event7);
        final Attendance attendance29 = new Attendance(Status.PRESENT, false, participant15, event7);

        // meeting 4
        final Attendance attendance30 = new Attendance(Status.TARDY, false, participant16, event9);
        final Attendance attendance31 = new Attendance(Status.TARDY, false, participant17, event9);
        final Attendance attendance32 = new Attendance(Status.TARDY, false, participant18, event9);
        final Attendance attendance33 = new Attendance(Status.PRESENT, false, participant19, event9);

        final Attendance attendance34 = new Attendance(Status.TARDY, false, participant16, event10);
        final Attendance attendance35 = new Attendance(Status.TARDY, false, participant17, event10);
        final Attendance attendance36 = new Attendance(Status.PRESENT, false, participant18, event10);
        final Attendance attendance37 = new Attendance(Status.PRESENT, false, participant19, event10);

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

        attendanceRepository.save(attendance22);
        attendanceRepository.save(attendance23);
        attendanceRepository.save(attendance24);
        attendanceRepository.save(attendance25);
        attendanceRepository.save(attendance26);
        attendanceRepository.save(attendance27);
        attendanceRepository.save(attendance28);
        attendanceRepository.save(attendance29);
        attendanceRepository.save(attendance30);
        attendanceRepository.save(attendance31);
        attendanceRepository.save(attendance32);
        attendanceRepository.save(attendance33);
        attendanceRepository.save(attendance34);
        attendanceRepository.save(attendance35);
        attendanceRepository.save(attendance36);
        attendanceRepository.save(attendance37);

    }
}
