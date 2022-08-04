package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @DisplayName("모임 일정을 저장한다")
    @Test
    void save() {
        // given
        final Meeting meeting = new Meeting("모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10));

        final Meeting savedMeeting = meetingRepository.save(meeting);

        final Event event = new Event(
                LocalDate.of(2022, 8, 8),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                savedMeeting);

        // when
        final Event savedEvent = eventRepository.save(event);

        // then
        assertThat(savedEvent.getId()).isNotNull();
    }

    @DisplayName("특정 날짜와 그 전의 모임 일정을 모두 조회한다.")
    @Test
    void findByMeetingIdAndDateLessThanEqual() {
        // given
        final Meeting meeting = new Meeting("모임1",
                LocalDate.of(2022, 8, 1),
                LocalDate.of(2022, 8, 10)
        );
        final LocalTime entranceTime = LocalTime.of(10, 0);
        final LocalTime leaveTime = LocalTime.of(18, 0);

        final Meeting savedMeeting = meetingRepository.save(meeting);
        final Event event1 = new Event(LocalDate.of(2022, 8, 3), entranceTime, leaveTime, savedMeeting);
        final Event event2 = new Event(LocalDate.of(2022, 8, 4), entranceTime, leaveTime, savedMeeting);
        final Event event3 = new Event(LocalDate.of(2022, 8, 5), entranceTime, leaveTime, savedMeeting);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        // when
        final List<Event> events = eventRepository.findByMeetingIdAndDateLessThanEqual(
                savedMeeting.getId(), LocalDate.of(2022, 8, 4));

        // then
        assertThat(events).containsExactlyInAnyOrder(event1, event2);
    }

    @DisplayName("특정 날짜의 모임 일정을 조회한다.")
    @Test
    void findByMeetingIdAndDate() {
        // given
        final LocalTime enteranceTime = LocalTime.of(10, 0);
        final LocalTime leaveTime = LocalTime.of(18, 0);
        final Meeting meeting = new Meeting("모임1",
                LocalDate.of(2022, 8, 1),
                LocalDate.of(2022, 8, 10)
        );

        final Meeting savedMeeting = meetingRepository.save(meeting);

        final Event event1 = new Event(
                LocalDate.of(2022, 8, 3), enteranceTime, leaveTime, savedMeeting);
        final Event event2 = new Event(
                LocalDate.of(2022, 8, 4), enteranceTime, leaveTime, savedMeeting);
        final Event event3 = new Event(
                LocalDate.of(2022, 8, 5), enteranceTime, leaveTime, savedMeeting);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        // when
        final Optional<Event> event = eventRepository.findByMeetingIdAndDate(
                savedMeeting.getId(), LocalDate.of(2022, 8, 4));
        assert (event.isPresent());

        // then
        assertThat(event.get()).isEqualTo(event2);
    }
}
