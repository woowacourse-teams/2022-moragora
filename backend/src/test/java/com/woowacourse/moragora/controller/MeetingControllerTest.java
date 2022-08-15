package com.woowacourse.moragora.controller;

import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.moragora.dto.EventResponse;
import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingsResponse;
import com.woowacourse.moragora.dto.ParticipantResponse;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.meeting.IllegalEntranceLeaveTimeException;
import com.woowacourse.moragora.exception.participant.InvalidParticipantException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class MeetingControllerTest extends ControllerTest {

    @DisplayName("미팅 방을 생성한다.")
    @Test
    void add() throws Exception {
        // given
        final Meeting meeting = MORAGORA.create();
        final List<Long> userIds = List.of(2L, 3L, 4L, 5L, 6L, 7L);

        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .userIds(userIds)
                .build();

        final Long loginId = validateToken("1");
        given(meetingService.save(any(MeetingRequest.class), eq(loginId)))
                .willReturn(1L);
        // when
        final ResultActions resultActions = performPost("/meetings", meetingRequest);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("/meetings/" + 1)))
                .andDo(document("meeting/create-meeting",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description(meeting.getName()),
                                fieldWithPath("userIds").type(JsonFieldType.ARRAY).description(userIds)
                        )
                ));
    }

    @DisplayName("미팅 방을 생성 시 시작 날짜보다 종료 날짜가 이른 경우 예외가 발생한다.")
    @Test
    void add_throwsException_ifStartDateIsLaterThanEndDate() throws Exception {
        // given
        final Meeting meeting = MORAGORA.create();
        final List<Long> userIds = List.of(2L, 3L, 4L, 5L, 6L, 7L);

        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .userIds(userIds)
                .build();

        final Long loginId = validateToken("1");
        given(meetingService.save(any(MeetingRequest.class), eq(loginId)))
                .willThrow(new IllegalEntranceLeaveTimeException());

        // when
        final ResultActions resultActions = performPost("/meetings", meetingRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("시작 시간보다 종료 시간이 이를 수 없습니다."));
    }

    @DisplayName("미팅 방 이름의 길이가 50자를 초과할 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"012345678901234567890123456789012345678901234567891",
            "영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영",
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija"})
    void add_throwsException_ifMeetingNameTooLong(final String name) throws Exception {
        // given
        final List<Long> userIds = List.of(2L, 3L, 4L, 5L, 6L, 7L);

        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(name)
                .userIds(userIds)
                .build();

        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/meetings", meetingRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("모임 이름은 50자를 초과할 수 없습니다."));
    }


    @DisplayName("미팅 방을 생성 시 참가자 명단에 중복 아이디가 있을 경우 예외가 발생한다.")
    @Test
    void add_throwsException_ifUserIdsDuplicate() throws Exception {
        // given
        final Meeting meeting = MORAGORA.create();
        final List<Long> userIds = List.of(2L, 3L, 4L, 5L, 6L, 7L);

        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .userIds(userIds)
                .build();

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
        final Meeting meeting = MORAGORA.create();
        final List<Long> userIds = List.of(2L, 3L, 4L, 5L, 6L, 7L);

        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .userIds(userIds)
                .build();

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
        final Meeting meeting = MORAGORA.create();
        final List<Long> userIds = List.of(2L, 3L, 4L, 5L, 6L, 7L);

        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .userIds(userIds)
                .build();

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
        final Meeting meeting = MORAGORA.create();
        final List<Long> userIds = List.of(2L, 3L, 4L, 5L, 6L, 7L);

        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .userIds(userIds)
                .build();

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
        participantResponses.add(new ParticipantResponse(1L, "abc@naver.com", "foo", 5, true));
        participantResponses.add(new ParticipantResponse(2L, "def@naver.com", "boo", 8, false));

        final long id = 1L;
        final String name = "모임1";
        final int attendanceCount = 0;
        final boolean isMaster = true;
        final MeetingResponse meetingResponse =
                new MeetingResponse(id, name, attendanceCount, isMaster, false, true, participantResponses);

        final Long loginId = validateToken("1");
        given(meetingService.findById(eq(1L), eq(loginId)))
                .willReturn(meetingResponse);

        // when
        final ResultActions resultActions = performGet("/meetings/1");

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("모임1")))
                .andExpect(jsonPath("$.attendedEventCount", equalTo(0)))
                .andExpect(jsonPath("$.isActive", equalTo(true)))
                .andExpect(jsonPath("$.isLoginUserMaster", equalTo(true)))
                .andExpect(jsonPath("$.isCoffeeTime", equalTo(false)))
                .andExpect(jsonPath("$.users[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.users[*].email", containsInAnyOrder("abc@naver.com", "def@naver.com")))
                .andExpect(jsonPath("$.users[*].nickname", containsInAnyOrder("foo", "boo")))
                .andExpect(jsonPath("$.users[*].tardyCount", containsInAnyOrder(5, 8)))
                .andExpect(jsonPath("$.users[*].isMaster", containsInAnyOrder(true, false)))
                .andDo(document("meeting/find-one-meeting",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("name").type(JsonFieldType.STRING).description(name),
                                fieldWithPath("attendedEventCount").type(JsonFieldType.NUMBER)
                                        .description(attendanceCount),
                                fieldWithPath("isActive").type(JsonFieldType.BOOLEAN).description(true),
                                fieldWithPath("isLoginUserMaster").type(JsonFieldType.BOOLEAN).description(isMaster),
                                fieldWithPath("isCoffeeTime").type(JsonFieldType.BOOLEAN).description(false),
                                fieldWithPath("users[].id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("users[].email").type(JsonFieldType.STRING).description("abc@email.com"),
                                fieldWithPath("users[].nickname").type(JsonFieldType.STRING).description("foo"),
                                fieldWithPath("users[].tardyCount").type(JsonFieldType.NUMBER).description(5),
                                fieldWithPath("users[].isMaster").type(JsonFieldType.BOOLEAN).description(true)
                        )
                ));
    }

    @DisplayName("유저가 소속된 모든 미팅 방을 조회한다.")
    @Test
    void findAllByUserId() throws Exception {
        // given
        final MyMeetingResponse myMeetingResponse =
                new MyMeetingResponse(1L, "모임1", 1, true, false, false,
                        new EventResponse(1L,
                                "09:30", "10:05",
                                "10:00", "18:00",
                                LocalDate.of(2022, 8, 1)
                        )
                );
        final MyMeetingsResponse meetingsResponse =
                new MyMeetingsResponse(List.of(myMeetingResponse));

        validateToken("1");

        // when
        given(meetingService.findAllByUserId(eq(1L)))
                .willReturn(meetingsResponse);

        // then
        performGet("/meetings/me")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetings[*].id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$.meetings[*].name", contains("모임1")))
                .andExpect(jsonPath("$.meetings[*].tardyCount", contains(1)))
                .andExpect(jsonPath("$.meetings[*].isLoginUserMaster", contains(true)))
                .andExpect(jsonPath("$.meetings[*].isCoffeeTime", contains(false)))
                .andExpect(jsonPath("$.meetings[*].isActive", contains(false)))
                .andExpect(jsonPath("$.meetings[*].upcomingEvent.id", contains(1)))
                .andExpect(jsonPath("$.meetings[*].upcomingEvent.attendanceOpenTime", contains("09:30")))
                .andExpect(jsonPath("$.meetings[*].upcomingEvent.attendanceClosedTime", contains("10:05")))
                .andExpect(jsonPath("$.meetings[*].upcomingEvent.meetingStartTime", contains("10:00")))
                .andExpect(jsonPath("$.meetings[*].upcomingEvent.meetingEndTime", contains("18:00")))
                .andExpect(jsonPath("$.meetings[*].upcomingEvent.date", contains("2022-08-01")))
                .andDo(document("meeting/find-my-meetings",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("meetings[].id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("meetings[].name").type(JsonFieldType.STRING).description("모임1"),
                                fieldWithPath("meetings[].tardyCount").type(JsonFieldType.NUMBER)
                                        .description(1),
                                fieldWithPath("meetings[].isLoginUserMaster").type(JsonFieldType.BOOLEAN)
                                        .description(true),
                                fieldWithPath("meetings[].isCoffeeTime").type(JsonFieldType.BOOLEAN)
                                        .description(false),
                                fieldWithPath("meetings[].isActive").type(JsonFieldType.BOOLEAN).description(true),
                                fieldWithPath("meetings[].upcomingEvent.id").type(JsonFieldType.NUMBER)
                                        .description(1),
                                fieldWithPath("meetings[].upcomingEvent.attendanceOpenTime").type(JsonFieldType.STRING)
                                        .description("09:30"),
                                fieldWithPath("meetings[].upcomingEvent.attendanceClosedTime").type(
                                                JsonFieldType.STRING)
                                        .description("10:05"),
                                fieldWithPath("meetings[].upcomingEvent.meetingStartTime").type(JsonFieldType.STRING)
                                        .description("10:00"),
                                fieldWithPath("meetings[].upcomingEvent.meetingEndTime").type(JsonFieldType.STRING)
                                        .description("18:00"),
                                fieldWithPath("meetings[].upcomingEvent.date").type(JsonFieldType.STRING)
                                        .description("2022-08-01")
                        )
                ));
    }

    @DisplayName("유저가 소속된 모든 미팅 방을 조회한다(다가오는 일정이 없으면 null 반환).")
    @Test
    void findAllByUserId_noUpcomingEvent() throws Exception {
        // given
        final MyMeetingResponse myMeetingResponse =
                new MyMeetingResponse(1L, "모임1", 2, true, false, true,
                        null);

        final MyMeetingsResponse meetingsResponse =
                new MyMeetingsResponse(List.of(myMeetingResponse));

        validateToken("1");

        // when
        given(meetingService.findAllByUserId(eq(1L)))
                .willReturn(meetingsResponse);

        // then
        performGet("/meetings/me")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetings[*].id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$.meetings[*].name", contains("모임1")))
                .andExpect(jsonPath("$.meetings[*].tardyCount", contains(2)))
                .andExpect(jsonPath("$.meetings[*].isLoginUserMaster", contains(true)))
                .andExpect(jsonPath("$.meetings[*].isCoffeeTime", contains(false)))
                .andExpect(jsonPath("$.meetings[*].isActive", contains(true)))
                .andExpect(jsonPath("$.meetings[*].upcomingEvent", contains(nullValue())));
    }

    @DisplayName("로그인한 유저가 참가중인 미팅에서 나간다.")
    @Test
    void deleteMeFrom() throws Exception {
        // given
        validateToken("1");

        // when
        final ResultActions resultActions = performDelete("/meetings/1/me");

        // then
        verify(meetingService, times(1)).deleteParticipant(anyLong(), anyLong());
        resultActions
                .andExpect(status().isNoContent())
                .andDo(document("meeting/delete-me", preprocessResponse(prettyPrint())));
    }

    @DisplayName("로그인한 유저가 자신이 마스터인 미팅에서 나가면 예외가 발생한다.")
    @Test
    void deleteMeFrom_throwsException_ifMaster() throws Exception {
        // given
        validateToken("1");
        doThrow(new ClientRuntimeException("마스터는 모임을 나갈 수 없습니다.", HttpStatus.FORBIDDEN))
                .when(meetingService).deleteParticipant(anyLong(), anyLong());

        // when
        final ResultActions resultActions = performDelete("/meetings/1/me");

        // then
        resultActions
                .andExpect(status().isForbidden());
    }
}
