package com.woowacourse.moragora.acceptance;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.util.CurrentDateTime;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("모임 관련 기능")
public class MeetingAcceptanceTest extends AcceptanceTest {

    @MockBean
    private CurrentDateTime currentDateTime;

    @DisplayName("사용자가 모임을 등록하고 상태코드 200 OK 를 반환받는다.")
    @Test
    void add() {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)
        );
        given(currentDateTime.getValue())
                .willReturn(LocalDateTime.of(2022, 7, 10, 0, 0));

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
        given(currentDateTime.getValue())
                .willReturn(LocalDateTime.now());

        // when
        final ValidatableResponse response = get("/meetings/" + id, signUpAndGetToken());

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", equalTo(id))
                .body("name", equalTo("모임1"))
                .body("attendanceCount", equalTo(4))
                .body("startDate", equalTo("2022-07-10"))
                .body("endDate", equalTo("2022-08-10"))
                .body("entranceTime", equalTo("10:00"))
                .body("leaveTime", equalTo("18:00"))
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
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)
        );
        final MeetingRequest meetingRequest2 = new MeetingRequest(
                "모임2",
                LocalDate.of(2022, 7, 13),
                LocalDate.of(2022, 8, 13),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)
        );
        final String token = signUpAndGetToken();

        given(currentDateTime.getValue())
                .willReturn(LocalDateTime.now());

        post("/meetings", meetingRequest1, token);
        post("/meetings", meetingRequest2, token);

        // when
        final ValidatableResponse response = get("/meetings/me", token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("meetings.id", containsInAnyOrder(2, 3))
                .body("meetings.name", containsInAnyOrder("모임1", "모임2"))
                .body("meetings.startDate", containsInAnyOrder("2022-07-10", "2022-07-13"))
                .body("meetings.endDate", containsInAnyOrder("2022-08-10", "2022-08-13"))
                .body("meetings.entranceTime", containsInAnyOrder("10:00", "09:00"))
                .body("meetings.closingTime", containsInAnyOrder("10:05", "09:05"))
                .body("meetings.tardyCount", containsInAnyOrder(0, 0));
    }

    @DisplayName("모임의 출석을 마감하면 총 모임 횟수와 결석한 참가자들의 결일을 증가시키고 상태코드 204을 반환한다.")
    @Test
    void endAttendance() {
        // given
        final int meetingId = 1;
        final int userId = 1;
        final UserAttendanceRequest userAttendanceRequest = new UserAttendanceRequest(Status.PRESENT);

        given(currentDateTime.getValue())
                .willReturn(LocalDateTime.of(2022, 7, 14, 0, 0));

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .auth().oauth2(signUpAndGetToken())
                .body(userAttendanceRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/meetings/" + meetingId + "/users/" + userId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }
}
