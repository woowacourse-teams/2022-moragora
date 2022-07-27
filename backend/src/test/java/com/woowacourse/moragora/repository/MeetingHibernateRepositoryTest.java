package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.repository.meeting.MeetingHibernateRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MeetingHibernateRepositoryTest {

    @Autowired
    private MeetingHibernateRepository meetingRepository;

    @DisplayName("미팅 방을 저장한다.")
    @Test
    void save() {
        // given
        final Meeting meeting = new Meeting("모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0));

        // when
        final Meeting savedMeeting = meetingRepository.save(meeting);

        // then
        assertThat(savedMeeting.getId()).isNotNull();
    }

    @DisplayName("미팅 정보를 조회한다.")
    @Test
    void findById() {
        // given, when
        final Meeting meeting = meetingRepository.findById(1L)
                .get();

        // then
        assertThat(meeting).isNotNull();
    }
}
