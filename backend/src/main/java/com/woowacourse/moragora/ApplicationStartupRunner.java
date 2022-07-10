package com.woowacourse.moragora;

import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.User;
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
    private final UserRepository userRepository;

    public ApplicationStartupRunner(final MeetingRepository meetingRepository,
                                    final UserRepository userRepository) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final Meeting meeting = new Meeting(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0));
        meetingRepository.save(meeting);

        userRepository.save(new User("아스피", meeting));
        userRepository.save(new User("필즈", meeting));
        userRepository.save(new User("포키", meeting));
        userRepository.save(new User("썬", meeting));
        userRepository.save(new User("우디", meeting));
        userRepository.save(new User("쿤", meeting));
        userRepository.save(new User("반듯", meeting));
    }
}
