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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class EventRepositoryTest {

    private static final LocalTime MEETING_START_TIME = LocalTime.of(10, 0);
    private static final LocalTime MEETING_END_TIME = LocalTime.of(18, 0);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @DisplayName("모임 일정을 저장한다")
    @Test
    void save() {
        // given
        final Meeting meeting = new Meeting("모임1");

        final Meeting savedMeeting = meetingRepository.save(meeting);

        final Event event = new Event(
                LocalDate.of(2022, 8, 8),
                MEETING_START_TIME, MEETING_END_TIME,
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
        final Meeting meeting = new Meeting("모임1");

        final Meeting savedMeeting = meetingRepository.save(meeting);
        final Event event1 = new Event(LocalDate.of(2022, 8, 3), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event2 = new Event(LocalDate.of(2022, 8, 4), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event3 = new Event(LocalDate.of(2022, 8, 5), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        // when
        final List<Event> events = eventRepository.findByMeetingIdAndDateLessThanEqual(
                savedMeeting.getId(), LocalDate.of(2022, 8, 4));

        // then
        assertThat(events).containsExactlyInAnyOrder(event1, event2);
    }

    @DisplayName("특정 날짜에 가장 가까운 모임 일정을 조회한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "8, 1, 8, 3",
            "8, 2, 8, 3",
            "8, 5, 8, 5"
    })
    void findFirstByMeetingIdAndDateGreaterThanEqualOrderByDate(final int month, final int day,
                                                                final int expectedMonth, final int expectedDay) {
        // given
        final Meeting meeting = new Meeting("모임1");

        final Meeting savedMeeting = meetingRepository.save(meeting);

        final Event event1 = new Event(
                LocalDate.of(2022, 8, 3), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event2 = new Event(
                LocalDate.of(2022, 8, 4), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event3 = new Event(
                LocalDate.of(2022, 8, 5), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        final LocalDate expected = LocalDate.of(2022, expectedMonth, expectedDay);
        // when
        final Optional<Event> event = eventRepository.findFirstByMeetingIdAndDateGreaterThanEqualOrderByDate(
                savedMeeting.getId(), LocalDate.of(2022, month, day));
        assert (event.isPresent());

        // then
        assertThat(event.get().isSameDate(expected)).isTrue();
    }

    @DisplayName("특정 날짜의 모임 일정을 조회한다.")
    @Test
    void findByMeetingIdAndDate() {
        // given
        final Meeting meeting = new Meeting("모임1");

        final Meeting savedMeeting = meetingRepository.save(meeting);

        final Event event1 = new Event(
                LocalDate.of(2022, 8, 3), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event2 = new Event(
                LocalDate.of(2022, 8, 4), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event3 = new Event(
                LocalDate.of(2022, 8, 5), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
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

    @DisplayName("모든 일정을 조회한다.")
    @Test
    void findByMeetingIdAndDuration_all() {
        // given
        final Meeting meeting = new Meeting("모임1");
        final Meeting savedMeeting = meetingRepository.save(meeting);

        final Event event1 = new Event(
                LocalDate.of(2022, 8, 3), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event2 = new Event(
                LocalDate.of(2022, 8, 4), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event3 = new Event(
                LocalDate.of(2022, 8, 5), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        // when
        final List<Event> events = eventRepository
                .findByMeetingIdAndDuration(meeting.getId(), null, null);
        // then
        assertThat(events).hasSize(3);
    }

    @DisplayName("특정 기간 이후의 일정을 조회한다.")
    @Test
    void findByMeetingIdAndDuration_isGreaterThanEqualBegin() {
        // given
        final Meeting meeting = new Meeting("모임1");
        final Meeting savedMeeting = meetingRepository.save(meeting);

        final Event event1 = new Event(
                LocalDate.of(2022, 8, 3), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event2 = new Event(
                LocalDate.of(2022, 8, 4), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event3 = new Event(
                LocalDate.of(2022, 8, 5), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        final LocalDate begin = LocalDate.of(2022, 8, 4);
        // when
        final List<Event> events = eventRepository
                .findByMeetingIdAndDuration(meeting.getId(), begin, null);
        // then
        assertThat(events).hasSize(2);
    }

    @DisplayName("특정 기간 이전의 일정을 조회한다.")
    @Test
    void findByMeetingIdAndDuration_isLessThanEqualEnd() {
        // given
        final Meeting meeting = new Meeting("모임1");
        final Meeting savedMeeting = meetingRepository.save(meeting);

        final Event event1 = new Event(
                LocalDate.of(2022, 8, 3), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event2 = new Event(
                LocalDate.of(2022, 8, 4), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event3 = new Event(
                LocalDate.of(2022, 8, 5), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        final LocalDate end = LocalDate.of(2022, 8, 4);

        // when
        final List<Event> events = eventRepository
                .findByMeetingIdAndDuration(meeting.getId(), null, end);
        // then
        assertThat(events).hasSize(2);
    }

    @DisplayName("특정 기간 이내의 일정을 조회한다.")
    @Test
    void findByMeetingIdAndDuration_isGreaterThanEqualBeginIsLessThanEqualEnd() {
        // given
        final Meeting meeting = new Meeting("모임1");
        final Meeting savedMeeting = meetingRepository.save(meeting);

        final Event event1 = new Event(
                LocalDate.of(2022, 8, 3), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event2 = new Event(
                LocalDate.of(2022, 8, 4), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        final Event event3 = new Event(
                LocalDate.of(2022, 8, 5), MEETING_START_TIME, MEETING_END_TIME, savedMeeting);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        final LocalDate begin = LocalDate.of(2022, 8, 4);
        final LocalDate end = LocalDate.of(2022, 8, 4);

        // when
        final List<Event> events = eventRepository
                .findByMeetingIdAndDuration(meeting.getId(), begin, end);
        // then
        assertThat(events).hasSize(1);
    }
}
