package com.woowacourse;

import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.entity.user.EncodedPassword;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.repository.AttendanceRepository;
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

    public ApplicationStartupRunner(final MeetingRepository meetingRepository,
                                    final UserRepository userRepository,
                                    final AttendanceRepository attendanceRepository,
                                    final ParticipantRepository participantRepository) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.participantRepository = participantRepository;
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

        final Meeting meeting = new Meeting(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0));
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

        final Attendance attendance1 = new Attendance(participant1, LocalDate.of(2022, 7, 12), Status.TARDY);
        final Attendance attendance2 = new Attendance(participant2, LocalDate.of(2022, 7, 12), Status.TARDY);
        final Attendance attendance3 = new Attendance(participant3, LocalDate.of(2022, 7, 12), Status.PRESENT);
        final Attendance attendance4 = new Attendance(participant4, LocalDate.of(2022, 7, 12), Status.PRESENT);
        final Attendance attendance5 = new Attendance(participant5, LocalDate.of(2022, 7, 12), Status.PRESENT);
        final Attendance attendance6 = new Attendance(participant6, LocalDate.of(2022, 7, 12), Status.PRESENT);
        final Attendance attendance7 = new Attendance(participant7, LocalDate.of(2022, 7, 12), Status.PRESENT);

        final Attendance attendance8 = new Attendance(participant1, LocalDate.of(2022, 7, 13), Status.PRESENT);
        final Attendance attendance9 = new Attendance(participant2, LocalDate.of(2022, 7, 13), Status.TARDY);
        final Attendance attendance10 = new Attendance(participant3, LocalDate.of(2022, 7, 13), Status.PRESENT);
        final Attendance attendance11 = new Attendance(participant4, LocalDate.of(2022, 7, 13), Status.PRESENT);
        final Attendance attendance12 = new Attendance(participant5, LocalDate.of(2022, 7, 13), Status.PRESENT);
        final Attendance attendance13 = new Attendance(participant6, LocalDate.of(2022, 7, 13), Status.PRESENT);
        final Attendance attendance14 = new Attendance(participant7, LocalDate.of(2022, 7, 13), Status.PRESENT);

        final Attendance attendance15 = new Attendance(participant1, LocalDate.of(2022, 7, 14), Status.PRESENT);
        final Attendance attendance16 = new Attendance(participant2, LocalDate.of(2022, 7, 14), Status.TARDY);
        final Attendance attendance17 = new Attendance(participant3, LocalDate.of(2022, 7, 14), Status.PRESENT);
        final Attendance attendance18 = new Attendance(participant4, LocalDate.of(2022, 7, 14), Status.PRESENT);
        final Attendance attendance19 = new Attendance(participant5, LocalDate.of(2022, 7, 14), Status.PRESENT);
        final Attendance attendance20 = new Attendance(participant6, LocalDate.of(2022, 7, 14), Status.PRESENT);
        final Attendance attendance21 = new Attendance(participant7, LocalDate.of(2022, 7, 14), Status.PRESENT);

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
