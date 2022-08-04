package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventsRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @DisplayName("모임 일정들을 저장한다.")
    @Test
    void save() {
        // given
        final long meetingId = 1L;
        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                LocalTime.of(10, 0),
                                LocalTime.of(18, 0),
                                LocalDate.of(2022, 8, 3)
                        ),
                        new EventRequest(
                                LocalTime.of(10, 0),
                                LocalTime.of(18, 0),
                                LocalDate.of(2022, 8, 4)
                        )
                ));

        // when, then
        assertThatCode(() -> eventService.save(eventsRequest, meetingId))
                .doesNotThrowAnyException();
    }
}
