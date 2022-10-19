package com.woowacourse.moragora.presentation;

import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
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

import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.dto.request.meeting.BeaconRequest;
import com.woowacourse.moragora.dto.request.meeting.BeaconsRequest;
import com.woowacourse.moragora.dto.request.meeting.MasterRequest;
import com.woowacourse.moragora.dto.request.meeting.MeetingRequest;
import com.woowacourse.moragora.dto.request.meeting.MeetingUpdateRequest;
import com.woowacourse.moragora.dto.response.event.EventResponse;
import com.woowacourse.moragora.dto.response.meeting.MeetingActiveResponse;
import com.woowacourse.moragora.dto.response.meeting.MeetingResponse;
import com.woowacourse.moragora.dto.response.meeting.MyMeetingResponse;
import com.woowacourse.moragora.dto.response.meeting.MyMeetingsResponse;
import com.woowacourse.moragora.dto.response.meeting.ParticipantResponse;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.meeting.IllegalEntranceLeaveTimeException;
import com.woowacourse.moragora.exception.participant.InvalidParticipantException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
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

    private static final String LENGTH_50_LETTER =
            "1abcdefghi" + "2abcdefghi" + "3abcdefghi" + "4abcdefghi" + "5abcdefghi";

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
                new MeetingResponse(id, name, attendanceCount, isMaster, false, participantResponses);

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
                                LocalTime.of(9, 30), LocalTime.of(10, 5),
                                LocalTime.of(10, 0), LocalTime.of(18, 0),
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

    @DisplayName("마스터 권한을 미팅의 다른 참가자에게 위임한다.")
    @Test
    void passMaster() throws Exception {
        // given
        final MasterRequest request = new MasterRequest(2L);
        validateToken("1");

        // when
        final ResultActions resultActions = performPut("/meetings/1/master", request);

        // then
        verify(meetingService, times(1)).assignMaster(anyLong(), any(MasterRequest.class), anyLong());
        resultActions.andExpect(status().isNoContent())
                .andDo(document("meeting/pass-master", preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description(1)
                        ))
                );
    }

    @DisplayName("마스터 권한을 스스로에게 위임하면 예외가 발생한다.")
    @Test
    void passMaster_throwsException_ifToMe() throws Exception {
        // given
        final MasterRequest request = new MasterRequest(1L);
        validateToken("1");
        doThrow(new ClientRuntimeException("스스로에게 마스터 권한을 넘길 수 없습니다.", HttpStatus.BAD_REQUEST))
                .when(meetingService).assignMaster(anyLong(), any(MasterRequest.class), anyLong());

        // when
        final ResultActions resultActions = performPut("/meetings/1/master", request);

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("미팅의 이름을 변경한다.")
    @Test
    void changeName() throws Exception {
        // given
        final MeetingUpdateRequest meetingUpdateRequest = new MeetingUpdateRequest("체크메이트");
        validateToken("1");

        // when
        final ResultActions resultActions = performPut("/meetings/1", meetingUpdateRequest);

        // then
        verify(meetingService, times(1)).updateName(any(MeetingUpdateRequest.class), anyLong());
        resultActions.andExpect(status().isNoContent())
                .andDo(document("meeting/change-name",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("체크메이트")
                        )
                ));
    }

    @DisplayName("미팅의 이름을 50자를 초과하는 이름으로 변경한다.")
    @ParameterizedTest
    @ValueSource(strings = {"012345678901234567890123456789012345678901234567891",
            "영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영",
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija"})
    void changeName_throwsException_ifTooLong(final String name) throws Exception {
        // given
        final MeetingUpdateRequest meetingUpdateRequest = new MeetingUpdateRequest(name);
        validateToken("1");

        // when
        final ResultActions resultActions = performPut("/meetings/1", meetingUpdateRequest);

        // then
        resultActions.andExpect(status().isBadRequest());
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
                .andDo(document("meeting/delete-me"));
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

    @DisplayName("미팅 삭제를 완료한다.")
    @Test
    void delete() throws Exception {
        // given
        validateToken("1");

        // when, then
        performDelete("/meetings/1")
                .andExpect(status().isNoContent());
    }

    @DisplayName("미팅을 삭제하려는 사람이 마스터가 아닌 경우 예외가 발생한다.")
    @Test
    void delete_throwsException_ifNotMaster() throws Exception {
        // given
        validateToken("1");

        doThrow(new ClientRuntimeException("마스터 권한이 없습니다.", HttpStatus.FORBIDDEN))
                .when(meetingService)
                .deleteMeeting(1L);

        // when, then
        performDelete("/meetings/1")
                .andExpect(status().isForbidden());
    }

    /**
     * 위치 기반 서비스
     */
    @DisplayName("비콘 등록시 주소가 비어있을 경우 예외를 반환한다.")
    @Test
    void attendWithBeaconBase_throwsException_ifEmptyAddress() throws Exception {
        // given
        final BeaconRequest beaconRequest = new BeaconRequest("", 37.0, 127.0, 100);
        final BeaconsRequest beaconsRequest = new BeaconsRequest(List.of(beaconRequest));
        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/meetings/1/beacons", beaconsRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("필수 입력 값이 누락됐습니다."));
    }

    @DisplayName("비콘 등록시 주소 길이가 50자를 넘으면 예외를 반환한다.")
    @Test
    void attendWithBeaconBase_throwsException_ifExceedAddressLength() throws Exception {
        // given
        final String exceed50LengthLetter = LENGTH_50_LETTER + "a";
        final BeaconRequest beaconRequest = new BeaconRequest(exceed50LengthLetter, 37.0, 127.0, 100);
        final BeaconsRequest beaconsRequest = new BeaconsRequest(List.of(beaconRequest));
        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/meetings/1/beacons", beaconsRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("비콘 주소는 50자를 초과할 수 없습니다."));
    }

    @DisplayName("비콘 등록시 비콘의 최소 반경 미만일 경우 예외를 반환한다.")
    @Test
    void attendWithBeaconBase_throwsException_ifUnderBeaconMinRadius() throws Exception {
        // given
        final BeaconRequest beaconRequest = new BeaconRequest("잠실나루", 37.0, 127.0, 10);
        final BeaconsRequest beaconsRequest = new BeaconsRequest(List.of(beaconRequest));
        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/meetings/1/beacons", beaconsRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("비콘의 반경은 최소 50m 이상이어야 합니다."));
    }

    @DisplayName("비콘 등록시 위도가 91을 초과할 경우 예외를 반환한다.")
    @Test
    void attendWithBeaconBase_throwsException_ifExceedLatitudeMax() throws Exception {
        // given
        final BeaconRequest beaconRequest = new BeaconRequest("잠실나루", 90.1, 127.0, 100);
        final BeaconsRequest beaconsRequest = new BeaconsRequest(List.of(beaconRequest));
        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/meetings/1/beacons", beaconsRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("위도는 +90(북위)에서 -90(남위)사이의 숫자를 넘길 수 없습니다"));
    }

    @DisplayName("비콘 등록시 동경이 180을 초과할 경우 예외를 반환한다.")
    @Test
    void attendWithBeaconBase_throwsException_ifExceedLongitudeMax() throws Exception {
        // given
        final BeaconRequest beaconRequest = new BeaconRequest("잠실나루", 37.0, 180.1, 100);
        final BeaconsRequest beaconsRequest = new BeaconsRequest(List.of(beaconRequest));
        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/meetings/1/beacons", beaconsRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("경도는 +180(서경)에서 -180(동경)사이의 숫자를 넘길 수 없습니다"));
    }

    @DisplayName("미팅이 체크인 활성화 상태인지 확인한다.")
    @Test
    void isActive() throws Exception {
        // given
        validateToken("1");

        final MeetingActiveResponse meetingActiveResponse = new MeetingActiveResponse(true);
        given(meetingService.checkActive(eq(1L)))
                .willReturn(meetingActiveResponse);

        // when, then
        performGet("/meetings/1/active")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive", equalTo(true)))
                .andDo(document("meeting/active", preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("isActive").type(JsonFieldType.BOOLEAN).description(true)
                        )
                ));
    }
}
