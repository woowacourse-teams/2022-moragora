package com.woowacourse.moragora;

import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.User;
import com.woowacourse.moragora.repository.AttendanceRepository;
import com.woowacourse.moragora.repository.MeetingRepository;
import com.woowacourse.moragora.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements ApplicationListener<ContextRefreshedEvent> {

    private final MeetingRepository meetingRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public ApplicationStartupRunner(final MeetingRepository meetingRepository,
                                    final AttendanceRepository attendanceRepository,
                                    final UserRepository userRepository) {
        this.meetingRepository = meetingRepository;
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        final User user1 = new User("아스피", "aaa111@foo.com");
        final User user2 = new User("필즈", "bbb222@foo.com");
        final User user3 = new User("포키", "ccc333@foo.com");
        final User user4 = new User("썬", "ddd444@foo.com");
        final User user5 = new User("우디", "eee555@foo.com");
        final User user6 = new User("쿤", "fff666@foo.com");
        final User user7 = new User("반듯", "ggg777@foo.com");

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

        final Attendance attendance1 = new Attendance(user1, meeting);
        final Attendance attendance2 = new Attendance(user2, meeting);
        final Attendance attendance3 = new Attendance(user3, meeting);
        final Attendance attendance4 = new Attendance(user4, meeting);
        final Attendance attendance5 = new Attendance(user5, meeting);
        final Attendance attendance6 = new Attendance(user6, meeting);
        final Attendance attendance7 = new Attendance(user7, meeting);

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);
        attendanceRepository.save(attendance3);
        attendanceRepository.save(attendance4);
        attendanceRepository.save(attendance5);
        attendanceRepository.save(attendance6);
        attendanceRepository.save(attendance7);
    }
}
