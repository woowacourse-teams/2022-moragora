package com.woowacourse.moragora.presentation;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.moragora.dto.request.event.EventCancelRequest;
import com.woowacourse.moragora.dto.request.event.EventRequest;
import com.woowacourse.moragora.dto.response.event.EventResponse;
import com.woowacourse.moragora.dto.request.event.EventsRequest;
import com.woowacourse.moragora.dto.response.event.EventsResponse;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.event.EventNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
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
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("events[].meetingStartTime").type(JsonFieldType.STRING)
                                        .description("10:00"),
                                fieldWithPath("events[].meetingEndTime").type(JsonFieldType.STRING)
                                        .description("18:00"),
                                fieldWithPath("events[].date").type(JsonFieldType.STRING)
                                        .description("2022-08-03")
                        ))
                );
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
        verify(eventService, times(1)).cancel(any(EventCancelRequest.class), any(Long.class));
        resultActions.andExpect(status().isNoContent())
                .andDo(document("event/cancel-event",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("dates").type(JsonFieldType.ARRAY)
                                        .description("[2022-08-03, 2022-08-04]")
                        )));
    }

    @DisplayName("모임의 가장 가까운 일정을 조회한다.")
    @Test
    void showUpcomingEvent() throws Exception {
        // given
        final EventResponse eventResponse = new EventResponse(
                1L,
                LocalTime.of(9, 30), LocalTime.of(10, 5),
                LocalTime.of(10, 0), LocalTime.of(18, 0),
                LocalDate.of(2022, 8, 1));
        validateToken("1");

        given(eventService.findUpcomingEvent(any(Long.class)))
                .willReturn(eventResponse);

        // when, then
        performGet("/meetings/1/events/upcoming")
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("attendanceOpenTime").value("09:30"))
                .andExpect(jsonPath("attendanceClosedTime").value("10:05"))
                .andExpect(jsonPath("meetingStartTime").value("10:00"))
                .andExpect(jsonPath("meetingEndTime").value("18:00"))
                .andExpect(jsonPath("date").value("2022-08-01"))
                .andDo(document("event/find-upcoming",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("attendanceOpenTime").type(JsonFieldType.STRING).description("09:30"),
                                fieldWithPath("attendanceClosedTime").type(JsonFieldType.STRING).description("10:05"),
                                fieldWithPath("meetingStartTime").type(JsonFieldType.STRING).description("10:00"),
                                fieldWithPath("meetingEndTime").type(JsonFieldType.STRING).description("18:00"),
                                fieldWithPath("date").type(JsonFieldType.STRING).description("2022-08-01")
                        )
                ));
    }

    @DisplayName("모임과 가장 가까운 일정 조회 시 모임의 다음 일정이 존재하지 않으면 예외가 발생한다.")
    @Test
    void showUpcomingEvent_ifEventNotFound() throws Exception {
        // given
        validateToken("1");

        given(eventService.findUpcomingEvent(any(Long.class)))
                .willThrow(new EventNotFoundException());

        // when, then
        final ResultActions resultActions = performGet("/meetings/1/events/upcoming")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("일정이 존재하지 않습니다."))
                .andDo(document("event/find-upcoming-not-found",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("일정이 존재하지 않습니다.")
                        )
                ));
    }

    @DisplayName("전체 일정을 조회한다.")
    @Test
    void showByDuration_all() throws Exception {
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
        given(eventService.findByDuration(anyLong(), nullable(LocalDate.class), nullable(LocalDate.class)))
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
                        preprocessResponse(prettyPrint()),
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
    void showByDuration_isGreaterThanEqualBegin() throws Exception {
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
        given(eventService.findByDuration(anyLong(), nullable(LocalDate.class), nullable(LocalDate.class)))
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
                        preprocessResponse(prettyPrint()),
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
    void showByDuration_isLessThanEqualEnd() throws Exception {
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
        given(eventService.findByDuration(anyLong(), nullable(LocalDate.class), nullable(LocalDate.class)))
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
                        preprocessResponse(prettyPrint()),
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
    void showByDuration_isGreaterThanEqualBeginIsLessThanEqualEnd() throws Exception {
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
        given(eventService.findByDuration(anyLong(), nullable(LocalDate.class), nullable(LocalDate.class)))
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
                        preprocessResponse(prettyPrint()),
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

    @DisplayName("이벤트 등록 시 오늘 이전의 이벤트를 등록하면 예외가 발생한다.")
    @Test
    void add_throwsException_ifPastEvent() throws Exception {
        // given
        doThrow(new ClientRuntimeException("기간의 입력이 잘못되었습니다.", HttpStatus.BAD_REQUEST))
                .when(eventService).save(any(EventsRequest.class), anyLong());
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
        resultActions.andExpect(status().isBadRequest());
    }


    @DisplayName("이벤트 등록 시 하루에 복수의 이벤트를 등록하면 예외가 발생한다.")
    @Test
    void add_throwsException_ifEventDateDuplicated() throws Exception {
        // given
        doThrow(new ClientRuntimeException("하루에 복수의 일정을 생성할 수 없습니다.", HttpStatus.BAD_REQUEST))
                .when(eventService).save(any(EventsRequest.class), anyLong());
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
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("이벤트 등록 시 하루에 복수의 이벤트를 등록하면 예외가 발생한다.")
    @Test
    void add_throwsException_ifAttendanceStartTimeIsAfterNow() throws Exception {
        // given
        doThrow(new ClientRuntimeException("출석 시간 전에 일정을 생성할 수 없습니다.", HttpStatus.BAD_REQUEST))
                .when(eventService).save(any(EventsRequest.class), anyLong());
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
        resultActions.andExpect(status().isBadRequest());
    }
}
