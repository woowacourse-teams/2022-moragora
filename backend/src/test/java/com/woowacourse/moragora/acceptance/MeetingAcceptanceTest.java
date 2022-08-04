package com.woowacourse.moragora.acceptance;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.support.ServerTimeManager;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@DisplayName("모임 관련 기능")
public class MeetingAcceptanceTest extends AcceptanceTest {

    @MockBean
    private ServerTimeManager serverTimeManager;

    @DisplayName("사용자가 모임을 등록하고 상태코드 200 OK 를 반환받는다.")
    @Test
    void add() {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)
        );

        // when
        final ValidatableResponse response = post("/meetings", meetingRequest, signUpAndGetToken());

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", notNullValue());
    }

    @DisplayName("사용자가 특정 모임을 조회하면 해당 모임 상세 정보와 상태코드 200을 반환한다.")
    @Test
    void findOne() {
        // given
        final int id = 1;
        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 0, 0);

        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
                .willReturn(false);
        given(serverTimeManager.getDate())
                .willReturn(dateTime.toLocalDate());
        given(serverTimeManager.getDateAndTime())
                .willReturn(dateTime);
        final LoginRequest loginRequest = new LoginRequest("aaa111@foo.com", "1234smart!");

        // when
        final ValidatableResponse response = get("/meetings/" + id, signInAndGetToken(loginRequest));

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", equalTo(id))
                .body("name", equalTo("모임1"))
                .body("attendanceCount", equalTo(3))
                .body("isMaster", equalTo(true))
                .body("users.id", containsInAnyOrder(1, 2, 3, 4, 5, 6, 7))
                .body("users.nickname", containsInAnyOrder("아스피", "필즈", "포키",
                        "썬", "우디", "쿤", "반듯"))
                .body("users.email", containsInAnyOrder("aaa111@foo.com",
                        "bbb222@foo.com",
                        "ccc333@foo.com",
                        "ddd444@foo.com",
                        "eee555@foo.com",
                        "fff666@foo.com",
                        "ggg777@foo.com"));
    }

    @DisplayName("사용자가 자신이 속한 모든 모임을 조회하면 모임 정보와 상태코드 200을 반환한다.")
    @Test
    void findMy() {
        // given
        final MeetingRequest meetingRequest1 = new MeetingRequest(
                "모임1",
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)
        );
        final MeetingRequest meetingRequest2 = new MeetingRequest(
                "모임2",
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)
        );
        final String token = signUpAndGetToken();
        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 0, 0);

        final ValidatableResponse post1 = post("/meetings", meetingRequest1, token);
        final ValidatableResponse post2 = post("/meetings", meetingRequest2, token);
        final Long meetingId1 = Long.parseLong(post1.extract().header("Location").split("/meetings/")[1]);
        final Long meetingId2 = Long.parseLong(post2.extract().header("Location").split("/meetings/")[1]);
        post("/meetings/" + meetingId1 + "/events", new EventsRequest(List.of(
                        new EventRequest(
                                LocalTime.of(10, 0),
                                LocalTime.of(18, 0),
                                LocalDate.of(2022, 7, 14)
                        ))),
                token);
        post("/meetings/" + meetingId2 + "/events", new EventsRequest(List.of(
                        new EventRequest(
                                LocalTime.of(9, 0),
                                LocalTime.of(17, 0),
                                LocalDate.of(2022, 7, 14)
                        ))),
                token);

        given(serverTimeManager.isAttendanceTime(LocalTime.of(10, 0)))
                .willReturn(false);
        given(serverTimeManager.isAttendanceTime(LocalTime.of(9, 0)))
                .willReturn(true);
        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
                .willReturn(true);
        given(serverTimeManager.calculateClosingTime(LocalTime.of(10, 0)))
                .willReturn(LocalTime.of(10, 5));
        given(serverTimeManager.calculateClosingTime(LocalTime.of(9, 0)))
                .willReturn(LocalTime.of(9, 5));
        given(serverTimeManager.getDate())
                .willReturn(dateTime.toLocalDate());

        // when
        final ValidatableResponse response = get("/meetings/me", token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("meetings.id", containsInAnyOrder(2, 3))
                .body("meetings.name", containsInAnyOrder("모임1", "모임2"))
                .body("meetings.isActive", containsInAnyOrder(false, true))
                .body("meetings.entranceTime", containsInAnyOrder("10:00", "09:00"))
                .body("meetings.closingTime", containsInAnyOrder("10:05", "09:05"))
                .body("meetings.tardyCount", containsInAnyOrder(1, 1))
                .body("meetings.isMaster", containsInAnyOrder(true, true));
    }
}
