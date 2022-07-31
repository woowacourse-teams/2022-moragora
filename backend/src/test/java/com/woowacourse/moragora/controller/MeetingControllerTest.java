package com.woowacourse.moragora.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingsResponse;
import com.woowacourse.moragora.dto.ParticipantResponse;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.exception.meeting.IllegalStartEndDateException;
import com.woowacourse.moragora.exception.participant.InvalidParticipantException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class MeetingControllerTest extends ControllerTest {

    @DisplayName("미팅 방을 생성한다.")
    @Test
    void add() throws Exception {
        // given
        final String name = "모임1";
        final LocalDate startDate = LocalDate.of(2022, 7, 10);
        final LocalDate endDate = LocalDate.of(2022, 8, 10);
        final LocalTime entranceTime = LocalTime.of(10, 0);
        final LocalTime leaveTime = LocalTime.of(18, 0);
        final List<Long> userIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

        final MeetingRequest meetingRequest = new MeetingRequest(name, startDate, endDate, entranceTime, leaveTime,
                userIds);

        final Long loginId = validateToken("1");
        given(meetingService.save(any(MeetingRequest.class), eq(loginId)))
                .willReturn(1L);
        // when
        final ResultActions resultActions = performPost("/meetings", meetingRequest);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("/meetings/" + 1)))
                .andDo(document("meeting/create-meeting",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description(name),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description(startDate),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description(endDate),
                                fieldWithPath("entranceTime").type(JsonFieldType.STRING).description(entranceTime),
                                fieldWithPath("leaveTime").type(JsonFieldType.STRING).description(leaveTime),
                                fieldWithPath("userIds").type(JsonFieldType.ARRAY).description(userIds)
                        )
                ));
    }

    @DisplayName("미팅 방을 생성 시 시작 날짜보다 종료 날짜가 이른 경우 예외가 발생한다.")
    @Test
    void add_throwsException_ifStartDateIsLaterThanEndDate() throws Exception {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 6, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)
        );

        final Long loginId = validateToken("1");
        given(meetingService.save(any(MeetingRequest.class), eq(loginId)))
                .willThrow(new IllegalStartEndDateException());

        // when
        final ResultActions resultActions = performPost("/meetings", meetingRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("시작 날짜보다 종료 날짜가 이를 수 없습니다."));
    }

    @DisplayName("미팅 방 이름의 길이가 50자를 초과할 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"012345678901234567890123456789012345678901234567891",
            "영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영",
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija"})
    void add_throwsException_ifMeetingNameTooLong(final String name) throws Exception {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                name,
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)
        );

        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/meetings", meetingRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("모임 이름은 50자를 초과할 수 없습니다."));
    }

    @DisplayName("날짜 또는 시간의 입력 형식이 올바르지 않은 경우 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "2022-02,2022-02-05,12:00,23:00",
            "2022-02-02,2022-02,12:00,23:00",
            "2022-02-02,2022-02-05,12,23:00",
            "2022-02-02,2022-02-05,12:00,23",
    })
    void add_throwsException_ifInvalidDateTimeFormat(final String startDate,
                                                     final String endDate,
                                                     final String entranceTime,
                                                     final String leaveTime) throws Exception {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "모임1");
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("entranceTime", entranceTime);
        params.put("leaveTime", leaveTime);

        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/meetings", params);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("입력 형식이 올바르지 않습니다."));
    }

    @DisplayName("미팅 방을 생성 시 참가자 명단에 중복 아이디가 있을 경우 예외가 발생한다.")
    @Test
    void add_throwsException_ifUserIdsDuplicate() throws Exception {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 6, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(2L, 2L, 3L, 4L, 5L, 6L, 7L)
        );
        final Long loginId = validateToken("1");
        given(meetingService.save(any(MeetingRequest.class), eq(loginId)))
                .willThrow(new InvalidParticipantException("참가자 명단에 중복이 있습니다."));

        // when
        final ResultActions resultActions = performPost("/meetings", meetingRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("참가자 명단에 중복이 있습니다."));
    }

    @DisplayName("미팅 방을 생성 시 참가자 명단이 비어있을 경우 예외가 발생한다.")
    @Test
    void add_throwsException_ifUserIdsEmpty() throws Exception {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 6, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of()
        );
        final Long loginId = validateToken("1");
        given(meetingService.save(any(MeetingRequest.class), eq(loginId)))
                .willThrow(new InvalidParticipantException("생성자를 제외한 참가자가 없습니다."));

        // when
        final ResultActions resultActions = performPost("/meetings", meetingRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("생성자를 제외한 참가자가 없습니다."));
    }

    @DisplayName("미팅 방을 생성 시 참가자 명단에 생성자 아이디가 있을 경우 예외가 발생한다.")
    @Test
    void add_throwsException_ifUserIdsContainLoginId() throws Exception {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 6, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)
        );
        final Long loginId = validateToken("1");
        given(meetingService.save(any(MeetingRequest.class), eq(loginId)))
                .willThrow(new InvalidParticipantException("생성자가 참가자 명단에 포함되어 있습니다."));

        // when
        final ResultActions resultActions = performPost("/meetings", meetingRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("생성자가 참가자 명단에 포함되어 있습니다."));
    }

    @DisplayName("미팅 방을 생성 시 참가자 명단에 존재하지 않는 아이디가 있을 경우 예외가 발생한다.")
    @Test
    void add_throwsException_ifUserIdNotExist() throws Exception {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 6, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 8L)
        );
        final Long loginId = validateToken("1");
        given(meetingService.save(any(MeetingRequest.class), eq(loginId)))
                .willThrow(new UserNotFoundException());

        // when
        final ResultActions resultActions = performPost("/meetings", meetingRequest);

        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("message")
                        .value("유저가 존재하지 않습니다."));
    }

    // TODO userResponse 테스트 작성
    @DisplayName("단일 미팅 방을 조회한다.")
    @Test
    void findOne() throws Exception {
        // given
        final List<ParticipantResponse> participantResponses = new ArrayList<>();
        participantResponses.add(new ParticipantResponse(1L, "abc@naver.com", "foo", Status.TARDY, 5));
        participantResponses.add(new ParticipantResponse(2L, "def@naver.com", "boo", Status.TARDY, 8));

        final long id = 1L;
        final String name = "모임1";
        final int attendanceCount = 0;
        final LocalDate startDate = LocalDate.of(2022, 7, 10);
        final LocalDate endDate = LocalDate.of(2022, 8, 10);
        final LocalTime entranceTime = LocalTime.of(10, 0);
        final LocalTime leaveTime = LocalTime.of(18, 0);
        final boolean isMaster = true;
        final MeetingResponse meetingResponse = new MeetingResponse(id, name, attendanceCount, startDate, endDate,
                entranceTime, leaveTime, isMaster, participantResponses
        );

        final Long loginId = validateToken("1");
        given(meetingService.findById(eq(1L), eq(loginId)))
                .willReturn(meetingResponse);

        // when
        final ResultActions resultActions = performGet("/meetings/1");

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("모임1")))
                .andExpect(jsonPath("$.attendanceCount", equalTo(0)))
                .andExpect(jsonPath("$.startDate", equalTo("2022-07-10")))
                .andExpect(jsonPath("$.endDate", equalTo("2022-08-10")))
                .andExpect(jsonPath("$.entranceTime", equalTo("10:00")))
                .andExpect(jsonPath("$.leaveTime", equalTo("18:00")))
                .andExpect(jsonPath("$.isMaster", equalTo(true)))
                .andExpect(jsonPath("$.users[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.users[*].email", containsInAnyOrder("abc@naver.com", "def@naver.com")))
                .andExpect(jsonPath("$.users[*].nickname", containsInAnyOrder("foo", "boo")))
                .andExpect(jsonPath("$.users[*].attendanceStatus", containsInAnyOrder("tardy", "tardy")))
                .andExpect(jsonPath("$.users[*].tardyCount", containsInAnyOrder(5, 8)))
                .andDo(document("meeting/find-one-meeting",
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("name").type(JsonFieldType.STRING).description(name),
                                fieldWithPath("attendanceCount").type(JsonFieldType.NUMBER)
                                        .description(attendanceCount),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description(startDate),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description(endDate),
                                fieldWithPath("entranceTime").type(JsonFieldType.STRING).description(entranceTime),
                                fieldWithPath("leaveTime").type(JsonFieldType.STRING).description(leaveTime),
                                fieldWithPath("isMaster").type(JsonFieldType.BOOLEAN).description(isMaster),
                                fieldWithPath("users[].id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("users[].email").type(JsonFieldType.STRING).description("abc@email.com"),
                                fieldWithPath("users[].nickname").type(JsonFieldType.STRING).description("foo"),
                                fieldWithPath("users[].attendanceStatus").type(JsonFieldType.STRING)
                                        .description("tardy"),
                                fieldWithPath("users[].tardyCount").type(JsonFieldType.NUMBER).description(5)
                        )
                ));
    }

    @DisplayName("유저가 소속된 모든 미팅 방을 조회한다.")
    @Test
    void findAllByUserId() throws Exception {
        // given
        final LocalDateTime now = LocalTime.of(10, 1).atDate(LocalDate.now());
        final MyMeetingResponse myMeetingResponse =
                new MyMeetingResponse(1L, "모임1", true,
                        LocalDate.of(2022, 7, 10),
                        LocalDate.of(2022, 8, 10),
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 5), 1);

        final MyMeetingResponse myMeetingResponse2 =
                new MyMeetingResponse(2L, "모임2", true,
                        LocalDate.of(2022, 7, 15),
                        LocalDate.of(2022, 8, 15),
                        LocalTime.of(9, 0),
                        LocalTime.of(9, 5), 2);

        final MyMeetingsResponse meetingsResponse =
                MyMeetingsResponse.of(now, List.of(myMeetingResponse, myMeetingResponse2));

        validateToken("1");

        // when
        given(meetingService.findAllByUserId(eq(1L)))
                .willReturn(meetingsResponse);

        // then
        performGet("/meetings/me")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serverTime", is(Timestamp.valueOf(now).getTime())))
                .andExpect(jsonPath("$.meetings[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.serverTime", is(Timestamp.valueOf(now).getTime())))
                .andExpect(jsonPath("$.meetings[*].name", containsInAnyOrder("모임1", "모임2")))
                .andExpect(jsonPath("$.meetings[*].isActive", containsInAnyOrder(true, true)))
                .andExpect(jsonPath("$.meetings[*].startDate", containsInAnyOrder("2022-07-10", "2022-07-15")))
                .andExpect(jsonPath("$.meetings[*].endDate", containsInAnyOrder("2022-08-10", "2022-08-15")))
                .andExpect(jsonPath("$.meetings[*].entranceTime", containsInAnyOrder("09:00", "10:00")))
                .andExpect(jsonPath("$.meetings[*].closingTime", containsInAnyOrder("09:05", "10:05")))
                .andExpect(jsonPath("$.meetings[*].tardyCount", containsInAnyOrder(1, 2)))
                .andDo(document("meeting/find-my-meetings",
                        responseFields(
                                fieldWithPath("serverTime").type(JsonFieldType.NUMBER)
                                        .description(Timestamp.valueOf(now).getTime()),
                                fieldWithPath("meetings[].id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("meetings[].name").type(JsonFieldType.STRING).description("모임1"),
                                fieldWithPath("meetings[].isActive").type(JsonFieldType.BOOLEAN).description(true),
                                fieldWithPath("meetings[].startDate").type(JsonFieldType.STRING)
                                        .description("2022-07-10"),
                                fieldWithPath("meetings[].endDate").type(JsonFieldType.STRING)
                                        .description("2022-08-10"),
                                fieldWithPath("meetings[].entranceTime").type(JsonFieldType.STRING)
                                        .description("09:00"),
                                fieldWithPath("meetings[].closingTime").type(JsonFieldType.STRING)
                                        .description("09:05"),
                                fieldWithPath("meetings[].tardyCount").type(JsonFieldType.NUMBER)
                                        .description(1)

                        )
                ));
    }
}
