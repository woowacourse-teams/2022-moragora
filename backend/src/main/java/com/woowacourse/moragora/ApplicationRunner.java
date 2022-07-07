package com.woowacourse.moragora;

import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.repository.MeetingRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements ApplicationListener<ContextRefreshedEvent> {

    private final MeetingRepository meetingRepository;

    public ApplicationRunner(final MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        meetingRepository.save(new Meeting());
    }
}
