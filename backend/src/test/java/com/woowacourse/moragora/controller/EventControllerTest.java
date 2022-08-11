package com.woowacourse.moragora.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventResponse;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.dto.EventsResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
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
                .andDo(document("event/add",
                        requestFields(
                                fieldWithPath("events[].entranceTime").type(JsonFieldType.STRING).description("10:00"),
                                fieldWithPath("events[].leaveTime").type(JsonFieldType.STRING).description("18:00"),
                                fieldWithPath("events[].date").type(JsonFieldType.STRING).description("2022-08-03")
                        )));
    }

    @DisplayName("전체 일정을 조회한다.")
    @Test
    void find_all() throws Exception {
        // given
        final LocalTime entranceTime = LocalTime.of(10, 0);
        final LocalTime leaveTime = LocalTime.of(18, 0);
        final EventsResponse eventsResponse = new EventsResponse(List.of(
                new EventResponse(1L,
                        entranceTime.minusMinutes(30),
                        entranceTime.plusMinutes(5),
                        entranceTime, leaveTime,
                        LocalDate.of(2022, 8, 1)
                ),
                new EventResponse(2L,
                        entranceTime.minusMinutes(30),
                        entranceTime.plusMinutes(5),
                        entranceTime, leaveTime,
                        LocalDate.of(2022, 8, 2)
                ),
                new EventResponse(3L,
                        entranceTime.minusMinutes(30),
                        entranceTime.plusMinutes(5),
                        entranceTime, leaveTime,
                        LocalDate.of(2022, 8, 3)
                )
        ));
        validateToken("1");
        given(eventService.inquireByDuration(anyLong(), nullable(LocalDate.class), nullable(LocalDate.class)))
                .willReturn(eventsResponse);

        // when
        final ResultActions resultActions = performGet("/meetings/1/events");

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.events[*].id", containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$.events[*].attendanceOpenTime",
                        containsInAnyOrder("09:30", "09:30", "09:30")))
                .andExpect(jsonPath("$.events[*].attendanceClosedTime",
                        containsInAnyOrder("10:05", "10:05", "10:05")))
                .andExpect(jsonPath("$.events[*].meetingStartTime",
                        containsInAnyOrder("10:00", "10:00", "10:00")))
                .andExpect(jsonPath("$.events[*].meetingEndTime",
                        containsInAnyOrder("18:00", "18:00", "18:00")))
                .andExpect(jsonPath("$.events[*].date",
                        containsInAnyOrder("2022-08-01", "2022-08-02", "2022-08-03")))
                .andDo(document("event/find-all",
                        responseFields(
                                fieldWithPath("events[].id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("events[].attendanceOpenTime").type(JsonFieldType.STRING)
                                        .description("09:30"),
                                fieldWithPath("events[].attendanceClosedTime").type(JsonFieldType.STRING)
                                        .description("10:05"),
                                fieldWithPath("events[].meetingStartTime").type(JsonFieldType.STRING)
                                        .description("10:00"),
                                fieldWithPath("events[].meetingEndTime").type(JsonFieldType.STRING)
                                        .description("18:00"),
                                fieldWithPath("events[].date").type(JsonFieldType.STRING).description("2022-08-01")
                        )));
    }

    @DisplayName("특정 날짜 이후의 일정을 조회한다.")
    @Test
    void find_isGreaterThanEqualBegin() throws Exception {
        // given
        final LocalTime entranceTime = LocalTime.of(10, 0);
        final LocalTime leaveTime = LocalTime.of(18, 0);
        final EventsResponse eventsResponse = new EventsResponse(List.of(
                new EventResponse(2L,
                        entranceTime.minusMinutes(30),
                        entranceTime.plusMinutes(5),
                        entranceTime, leaveTime,
                        LocalDate.of(2022, 8, 2)
                ),
                new EventResponse(3L,
                        entranceTime.minusMinutes(30),
                        entranceTime.plusMinutes(5),
                        entranceTime, leaveTime,
                        LocalDate.of(2022, 8, 3)
                )
        ));
        validateToken("1");
        given(eventService.inquireByDuration(anyLong(), nullable(LocalDate.class), nullable(LocalDate.class)))
                .willReturn(eventsResponse);

        // when
        final ResultActions resultActions = performGet("/meetings/1/events?begin=2022-08-02");

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.events[*].id", containsInAnyOrder(2, 3)))
                .andExpect(jsonPath("$.events[*].attendanceOpenTime",
                        containsInAnyOrder("09:30", "09:30")))
                .andExpect(jsonPath("$.events[*].attendanceClosedTime",
                        containsInAnyOrder("10:05", "10:05")))
                .andExpect(jsonPath("$.events[*].meetingStartTime",
                        containsInAnyOrder("10:00", "10:00")))
                .andExpect(jsonPath("$.events[*].meetingEndTime",
                        containsInAnyOrder("18:00", "18:00")))
                .andExpect(jsonPath("$.events[*].date",
                        containsInAnyOrder("2022-08-02", "2022-08-03")))
                .andDo(document("event/find-isGreaterThanEqualBegin",
                        responseFields(
                                fieldWithPath("events[].id").type(JsonFieldType.NUMBER).description(2L),
                                fieldWithPath("events[].attendanceOpenTime").type(JsonFieldType.STRING)
                                        .description("09:30"),
                                fieldWithPath("events[].attendanceClosedTime").type(JsonFieldType.STRING)
                                        .description("10:05"),
                                fieldWithPath("events[].meetingStartTime").type(JsonFieldType.STRING)
                                        .description("10:00"),
                                fieldWithPath("events[].meetingEndTime").type(JsonFieldType.STRING)
                                        .description("18:00"),
                                fieldWithPath("events[].date").type(JsonFieldType.STRING).description("2022-08-02")
                        )));
    }

    @DisplayName("특정 날짜 이전의 일정을 조회한다.")
    @Test
    void find_isLessThanEqualEnd() throws Exception {
        // given
        final LocalTime entranceTime = LocalTime.of(10, 0);
        final LocalTime leaveTime = LocalTime.of(18, 0);
        final EventsResponse eventsResponse = new EventsResponse(List.of(
                new EventResponse(1L,
                        entranceTime.minusMinutes(30),
                        entranceTime.plusMinutes(5),
                        entranceTime, leaveTime,
                        LocalDate.of(2022, 8, 1)
                ),
                new EventResponse(2L,
                        entranceTime.minusMinutes(30),
                        entranceTime.plusMinutes(5),
                        entranceTime, leaveTime,
                        LocalDate.of(2022, 8, 2)
                )
        ));
        validateToken("1");
        given(eventService.inquireByDuration(anyLong(), nullable(LocalDate.class), nullable(LocalDate.class)))
                .willReturn(eventsResponse);

        // when
        final ResultActions resultActions = performGet("/meetings/1/events?end=2022-08-02");

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.events[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.events[*].attendanceOpenTime",
                        containsInAnyOrder("09:30", "09:30")))
                .andExpect(jsonPath("$.events[*].attendanceClosedTime",
                        containsInAnyOrder("10:05", "10:05")))
                .andExpect(jsonPath("$.events[*].meetingStartTime",
                        containsInAnyOrder("10:00", "10:00")))
                .andExpect(jsonPath("$.events[*].meetingEndTime",
                        containsInAnyOrder("18:00", "18:00")))
                .andExpect(jsonPath("$.events[*].date",
                        containsInAnyOrder("2022-08-01", "2022-08-02")))
                .andDo(document("event/find-isLessThanEqualEnd",
                        responseFields(
                                fieldWithPath("events[].id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("events[].attendanceOpenTime").type(JsonFieldType.STRING)
                                        .description("09:30"),
                                fieldWithPath("events[].attendanceClosedTime").type(JsonFieldType.STRING)
                                        .description("10:05"),
                                fieldWithPath("events[].meetingStartTime").type(JsonFieldType.STRING)
                                        .description("10:00"),
                                fieldWithPath("events[].meetingEndTime").type(JsonFieldType.STRING)
                                        .description("18:00"),
                                fieldWithPath("events[].date").type(JsonFieldType.STRING).description("2022-08-01")
                        )));
    }

    @DisplayName("특정 기간의 일정을 조회한다.")
    @Test
    void find_inDuration() throws Exception {
        // given
        final LocalTime entranceTime = LocalTime.of(10, 0);
        final LocalTime leaveTime = LocalTime.of(18, 0);
        final EventsResponse eventsResponse = new EventsResponse(List.of(
                new EventResponse(2L,
                        entranceTime.minusMinutes(30),
                        entranceTime.plusMinutes(5),
                        entranceTime, leaveTime,
                        LocalDate.of(2022, 8, 2)
                )
        ));
        validateToken("1");
        given(eventService.inquireByDuration(anyLong(), nullable(LocalDate.class), nullable(LocalDate.class)))
                .willReturn(eventsResponse);

        // when
        final ResultActions resultActions = performGet("/meetings/1/events?begin=2022-08-02&end=2022-08-02");

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.events[*].id", containsInAnyOrder(2)))
                .andExpect(jsonPath("$.events[*].attendanceOpenTime", containsInAnyOrder("09:30")))
                .andExpect(jsonPath("$.events[*].attendanceClosedTime", containsInAnyOrder("10:05")))
                .andExpect(jsonPath("$.events[*].meetingStartTime", containsInAnyOrder("10:00")))
                .andExpect(jsonPath("$.events[*].meetingEndTime", containsInAnyOrder("18:00")))
                .andExpect(jsonPath("$.events[*].date", containsInAnyOrder("2022-08-02")))
                .andDo(document("event/find-inDuration",
                        responseFields(
                                fieldWithPath("events[].id").type(JsonFieldType.NUMBER).description(2L),
                                fieldWithPath("events[].attendanceOpenTime").type(JsonFieldType.STRING)
                                        .description("09:30"),
                                fieldWithPath("events[].attendanceClosedTime").type(JsonFieldType.STRING)
                                        .description("10:05"),
                                fieldWithPath("events[].meetingStartTime").type(JsonFieldType.STRING)
                                        .description("10:00"),
                                fieldWithPath("events[].meetingEndTime").type(JsonFieldType.STRING)
                                        .description("18:00"),
                                fieldWithPath("events[].date").type(JsonFieldType.STRING).description("2022-08-02")
                        )));
    }
}
