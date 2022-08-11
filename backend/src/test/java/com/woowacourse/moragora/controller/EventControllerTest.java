package com.woowacourse.moragora.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.moragora.dto.EventCancelRequest;
import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventsRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class EventControllerTest extends ControllerTest {

    @DisplayName("일정들을 등록한다.")
    @Test
    void add() throws Exception {
        // given
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
        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/meetings/1/events", eventsRequest);

        // then
        verify(eventService, times(1)).save(any(EventsRequest.class), any(Long.class));
        resultActions.andExpect(status().isNoContent())
                .andDo(document("event/add"));
    }

    @DisplayName("일정들을 삭제한다.")
    @Test
    void cancel() throws Exception {
        // given
        final EventCancelRequest eventCancelRequest = new EventCancelRequest(List.of(
                LocalDate.of(2022, 8, 3),
                LocalDate.of(2022, 8, 4)
        ));

        validateToken("1");

        // when
        final ResultActions resultActions = performDelete("/meetings/1/events", eventCancelRequest);

        // then
        verify(eventService, times(1)).delete(any(EventCancelRequest.class), any(Long.class));
        resultActions.andExpect(status().isNoContent())
                .andDo(document("event/cancel-event"));
    }
}
