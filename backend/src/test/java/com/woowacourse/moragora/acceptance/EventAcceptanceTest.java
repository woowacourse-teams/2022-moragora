package com.woowacourse.moragora.acceptance;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.EventFixtures.EVENT3;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.AZPI;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.MASTER;
import static com.woowacourse.moragora.support.UserFixtures.NO_MASTER;
import static com.woowacourse.moragora.support.UserFixtures.createUsers;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.moragora.dto.EventCancelRequest;
import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.support.ServerTimeManager;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

class EventAcceptanceTest extends AcceptanceTest {

    @MockBean
    private ServerTimeManager serverTimeManager;

    @DisplayName("특정 모임에 대한 일정들을 등록하고 상태코드 200을 반환한다.")
    @Test
    void add() {
        // given
        given(serverTimeManager.getDate())
                .willReturn(LocalDate.of(2022, 7, 31));
        given(serverTimeManager.calculateAttendanceCloseTime(any(LocalTime.class)))
                .willReturn(LocalTime.of(10, 5));

        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);

        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getStartTime(),
                                event1.getEndTime(),
                                event1.getDate()
                        ),
                        new EventRequest(
                                event2.getStartTime(),
                                event2.getEndTime(),
                                event2.getDate()
                        )
                ));

        // when
        final ValidatableResponse response = post("/meetings/" + meetingId + "/events", eventsRequest, token);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("마스터 권한이 없는 참가자가 일정 등록을 요청하는 경우 상태코드 403을 반환한다.")
    @Test
    void add_notMaster() {
        // given
        final User master = MASTER.create();
        final User noMaster = NO_MASTER.create();
        final User user = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(noMaster, user));

        final String masterToken = signUpAndGetToken(master);

        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(masterToken, userIds, meeting);

        final LoginRequest noMasterLoginRequest = new LoginRequest(noMaster.getEmail(), "1234asdf!");
        final String token = post("/login", noMasterLoginRequest)
                .extract().jsonPath().get("accessToken");

        final Event event1 = EVENT1.create(meeting);
        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getStartTime(),
                                event1.getEndTime(),
                                event1.getDate()
                        )
                ));
        // when
        final ValidatableResponse response = post("/meetings/" + meetingId + "/events", eventsRequest, token);

        // then
        response.statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("특정 모임에 대한 일정을 선택적으로 삭제하고 상태코드 204를 반환한다.")
    @Test
    void cancel() {
        // given
        final String token = signUpAndGetToken(MASTER.create());
        final List<Long> userIds = saveUsers(createUsers());
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);
        saveEvents(token, List.of(EVENT1.create(meeting), EVENT2.create(meeting)), (long) meetingId);

        // when
        final EventCancelRequest eventCancelRequest = new EventCancelRequest(
                List.of(EVENT1.getDate(), EVENT2.getDate()));

        final ValidatableResponse response = delete("/meetings/" + meetingId + "/events", eventCancelRequest, token);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("마스터가 아닌 참가자가 일정 삭제 요청을 하면 상태코드 403을 반환한다.")
    @Test
    void cancel_notMaster() {
        // given
        final User master = MASTER.create();
        final User noMaster = NO_MASTER.create();
        final User user = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(noMaster, user));

        final String masterToken = signUpAndGetToken(master);

        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(masterToken, userIds, meeting);

        final LoginRequest noMasterLoginRequest = new LoginRequest(noMaster.getEmail(), "1234asdf!");
        final String token = post("/login", noMasterLoginRequest)
                .extract().jsonPath().get("accessToken");

        final Event event1 = EVENT1.create(meeting);
        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getStartTime(),
                                event1.getEndTime(),
                                event1.getDate()
                        )
                ));
        post("/meetings/" + meetingId + "/events", eventsRequest, masterToken);

        // when
        final EventCancelRequest eventCancelRequest = new EventCancelRequest(
                List.of(EVENT1.getDate(), EVENT2.getDate()));

        final ValidatableResponse response = delete("/meetings/" + meetingId + "/events", eventCancelRequest, token);

        // then
        response.statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("모임의 가장 가까운 일정을 요청하면 일정 상세 정보와 상태코드 200을 반환한다.")
    @Test
    void showUpcomingEvent() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);

        given(serverTimeManager.getDate())
                .willReturn(LocalDate.of(2022, 7, 31));
        given(serverTimeManager.calculateOpenTime(event1.getStartTime()))
                .willReturn(event1.getStartTime().minusMinutes(30));
        given(serverTimeManager.calculateAttendanceCloseTime(event1.getStartTime()))
                .willReturn(event1.getStartTime().plusMinutes(5));

        saveEvents(token, List.of(event1, event2), (long) meetingId);

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/events/upcoming", token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", equalTo(1))
                .body("attendanceOpenTime", equalTo("09:30"))
                .body("attendanceClosedTime", equalTo("10:05"))
                .body("meetingStartTime", equalTo("10:00"))
                .body("meetingEndTime", equalTo("18:00"))
                .body("date", equalTo("2022-08-01"));
    }

    @DisplayName("모임의 가장 가까운 일정이 존재하지 않은 경우에  일정 상세 정보와 상태코드 404를 반환한다.")
    @Test
    void showUpcomingEvent_ifEventNotFound() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        saveEvents(token, List.of(event1, event2), (long) meetingId);

        given(serverTimeManager.getDate())
                .willReturn(LocalDate.of(2022, 8, 3));

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/events/upcoming", token);

        // then
        response.statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("일정이 존재하지 않습니다."));
    }

    @DisplayName("모든 이벤트를 조회한다.")
    @Test
    void showByDuration_all() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);

        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getStartTime(),
                                event1.getEndTime(),
                                event1.getDate()
                        ),
                        new EventRequest(
                                event2.getStartTime(),
                                event2.getEndTime(),
                                event2.getDate()
                        )
                ));
        post("/meetings/" + meetingId + "/events", eventsRequest, token);

        given(serverTimeManager.calculateOpenTime(event1.getStartTime()))
                .willReturn(event1.getStartTime().minusMinutes(30));
        given(serverTimeManager.calculateAttendanceCloseTime(event1.getStartTime()))
                .willReturn(event1.getStartTime().plusMinutes(5));
        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/events", token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("events.attendanceOpenTime",
                        containsInAnyOrder("09:30", "09:30"))
                .body("events.attendanceClosedTime",
                        containsInAnyOrder("10:05", "10:05"))
                .body("events.meetingStartTime",
                        containsInAnyOrder("10:00", "10:00"))
                .body("events.meetingEndTime",
                        containsInAnyOrder("18:00", "18:00"))
                .body("events.date", containsInAnyOrder("2022-08-01", "2022-08-02"));
    }

    @DisplayName("특정 날짜 이후의 일정을 조회한다.")
    @Test
    void showByDuration_isGreaterThanEqualBegin() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        final Event event3 = EVENT3.create(meeting);

        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getStartTime(),
                                event1.getEndTime(),
                                event1.getDate()
                        ),
                        new EventRequest(
                                event2.getStartTime(),
                                event2.getEndTime(),
                                event2.getDate()
                        ),
                        new EventRequest(
                                event3.getStartTime(),
                                event3.getEndTime(),
                                event3.getDate()
                        )
                ));
        post("/meetings/" + meetingId + "/events", eventsRequest, token);

        given(serverTimeManager.calculateOpenTime(event1.getStartTime()))
                .willReturn(event1.getStartTime().minusMinutes(30));
        given(serverTimeManager.calculateAttendanceCloseTime(event1.getStartTime()))
                .willReturn(event1.getStartTime().plusMinutes(5));

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/events?begin=" + event2.getDate(), token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("events.attendanceOpenTime",
                        containsInAnyOrder("09:30", "09:30"))
                .body("events.attendanceClosedTime",
                        containsInAnyOrder("10:05", "10:05"))
                .body("events.meetingStartTime",
                        containsInAnyOrder("10:00", "10:00"))
                .body("events.meetingEndTime",
                        containsInAnyOrder("18:00", "18:00"))
                .body("events.date", containsInAnyOrder("2022-08-02", "2022-08-03"));
    }

    @DisplayName("특정 날짜 이전의 일정을 조회한다.")
    @Test
    void showByDuration_isLessThanEqualEnd() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        final Event event3 = EVENT3.create(meeting);

        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getStartTime(),
                                event1.getEndTime(),
                                event1.getDate()
                        ),
                        new EventRequest(
                                event2.getStartTime(),
                                event2.getEndTime(),
                                event2.getDate()
                        ),
                        new EventRequest(
                                event3.getStartTime(),
                                event3.getEndTime(),
                                event3.getDate()
                        )
                ));
        post("/meetings/" + meetingId + "/events", eventsRequest, token);

        given(serverTimeManager.calculateOpenTime(event1.getStartTime()))
                .willReturn(event1.getStartTime().minusMinutes(30));
        given(serverTimeManager.calculateAttendanceCloseTime(event1.getStartTime()))
                .willReturn(event1.getStartTime().plusMinutes(5));

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/events?end=" + event2.getDate(), token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("events.attendanceOpenTime",
                        containsInAnyOrder("09:30", "09:30"))
                .body("events.attendanceClosedTime",
                        containsInAnyOrder("10:05", "10:05"))
                .body("events.meetingStartTime",
                        containsInAnyOrder("10:00", "10:00"))
                .body("events.meetingEndTime",
                        containsInAnyOrder("18:00", "18:00"))
                .body("events.date", containsInAnyOrder("2022-08-01", "2022-08-02"));
    }

    @DisplayName("특정 기간의 일정을 조회한다.")
    @Test
    void showByDuration_isGreaterThanEqualBeginIsLessThanEqualEnd() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        final Event event3 = EVENT3.create(meeting);

        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getStartTime(),
                                event1.getEndTime(),
                                event1.getDate()
                        ),
                        new EventRequest(
                                event2.getStartTime(),
                                event2.getEndTime(),
                                event2.getDate()
                        ),
                        new EventRequest(
                                event3.getStartTime(),
                                event3.getEndTime(),
                                event3.getDate()
                        )
                ));
        post("/meetings/" + meetingId + "/events", eventsRequest, token);

        given(serverTimeManager.calculateOpenTime(event1.getStartTime()))
                .willReturn(event1.getStartTime().minusMinutes(30));
        given(serverTimeManager.calculateAttendanceCloseTime(event1.getStartTime()))
                .willReturn(event1.getStartTime().plusMinutes(5));

        // when
        final ValidatableResponse response = get(
                "/meetings/" + meetingId + "/events?begin=" + event2.getDate() + "&end=" + event2.getDate(), token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("events.attendanceOpenTime",
                        containsInAnyOrder("09:30"))
                .body("events.attendanceClosedTime",
                        containsInAnyOrder("10:05"))
                .body("events.meetingStartTime",
                        containsInAnyOrder("10:00"))
                .body("events.meetingEndTime",
                        containsInAnyOrder("18:00"))
                .body("events.date", containsInAnyOrder("2022-08-02"));
    }
}
