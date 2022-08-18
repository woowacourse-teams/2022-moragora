package com.woowacourse.moragora.repository;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.EventFixtures.EVENT3;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.support.DataSupport;
import java.time.LocalDate;
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

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private DataSupport dataSupport;

    @DisplayName("모임 일정을 저장한다")
    @Test
    void save() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final Event event = EVENT1.create(meeting);

        // when
        final Event savedEvent = eventRepository.save(event);

        // then
        assertThat(savedEvent.getId()).isNotNull();
    }

    @DisplayName("특정 날짜 까지의 일정 개수를 조회한다.")
    @Test
    void countByMeetingIdAndDateLessThanEqual() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveEvent(EVENT3.create(meeting));

        // when
        final long count = eventRepository.countByMeetingIdAndDateLessThanEqual(
                meeting.getId(), LocalDate.of(2022, 8, 2));

        // then
        assertThat(count).isEqualTo(2);
    }

    @DisplayName("특정 날짜에 가장 가까운 모임 일정을 조회한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "7, 31, 8, 1",
            "8, 1, 8, 1",
            "8, 3, 8, 3"
    })
    void findFirstByMeetingIdAndDateGreaterThanEqualOrderByDate(final int month, final int day,
                                                                final int expectedMonth, final int expectedDay) {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveEvent(EVENT3.create(meeting));

        final LocalDate expected = LocalDate.of(2022, expectedMonth, expectedDay);
        // when
        final Optional<Event> event = eventRepository.findFirstByMeetingIdAndDateGreaterThanEqualOrderByDate(
                meeting.getId(), LocalDate.of(2022, month, day));
        assert (event.isPresent());

        // then
        assertThat(event.get().isSameDate(expected)).isTrue();
    }

    @DisplayName("특정 날짜의 모임 일정을 조회한다.")
    @Test
    void findByMeetingIdAndDate() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveEvent(EVENT3.create(meeting));

        final LocalDate date = LocalDate.of(2022, 8, 2);

        // when
        final Optional<Event> event = eventRepository.findByMeetingIdAndDate(
                meeting.getId(), date);
        assert (event.isPresent());

        // then
        assertThat(event.get().isSameDate(date)).isTrue();
    }

    @DisplayName("모든 일정을 조회한다.")
    @Test
    void findByMeetingIdAndDuration_all() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveEvent(EVENT3.create(meeting));

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
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveEvent(EVENT3.create(meeting));

        final LocalDate begin = LocalDate.of(2022, 8, 2);
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
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveEvent(EVENT3.create(meeting));

        final LocalDate end = LocalDate.of(2022, 8, 2);

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
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveEvent(EVENT3.create(meeting));

        final LocalDate begin = LocalDate.of(2022, 8, 2);
        final LocalDate end = LocalDate.of(2022, 8, 2);

        // when
        final List<Event> events = eventRepository
                .findByMeetingIdAndDuration(meeting.getId(), begin, end);
        // then
        assertThat(events).hasSize(1);
    }
}
