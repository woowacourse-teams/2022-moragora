package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.support.MeetingFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MeetingRepositoryTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @DisplayName("미팅 방을 저장한다.")
    @Test
    void save() {
        // given
        final Meeting meeting = MeetingFixtures.MORAGORA.create();

        // when
        final Meeting savedMeeting = meetingRepository.save(meeting);

        // then
        assertThat(savedMeeting.getId()).isNotNull();
    }

    @DisplayName("미팅 정보를 조회한다.")
    @Test
    void findById() {
        // given, when
        final Meeting meeting = meetingRepository.save(MeetingFixtures.MORAGORA.create());
        final Meeting foundMeeting = meetingRepository.findById(meeting.getId())
                .get();

        // then
        assertThat(foundMeeting).isNotNull();
    }
}
