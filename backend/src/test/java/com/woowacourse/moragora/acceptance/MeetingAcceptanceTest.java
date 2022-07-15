package com.woowacourse.moragora.acceptance;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.dto.UserAttendancesRequest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("모임 관련 기능")
public class MeetingAcceptanceTest extends AcceptanceTest {

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

        // when
        final ValidatableResponse response = post("/meetings", meetingRequest);




        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value())
                .header("Location", notNullValue());
    }

    @DisplayName("사용자가 특정 모임을 조회하면 해당 모임 상세 정보와 상태코드 200을 반환한다.")
    @Test
    void findOne() {
        // given
        final int id = 1;

        // when
        final ValidatableResponse response = get("/meetings/" + id);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", equalTo(id))
                .body("name", equalTo("모임1"))
                .body("attendanceCount", equalTo(0))
                .body("startDate", equalTo("2022-07-10"))
                .body("endDate", equalTo("2022-08-10"))
                .body("entranceTime", equalTo("10:00:00"))
                .body("leaveTime", equalTo("18:00:00"))
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

    @DisplayName("모임의 출석을 마감하면 총 모임 횟수와 결석한 참가자들의 결일을 증가시키고 상태코드 204을 반환한다.")
    @Test
    void endAttendance() {
        // given
        final int id = 1;
        final UserAttendancesRequest userAttendancesRequest = new UserAttendancesRequest(List.of(
                new UserAttendanceRequest(1L, false),
                new UserAttendanceRequest(2L, false),
                new UserAttendanceRequest(3L, true),
                new UserAttendanceRequest(4L, true),
                new UserAttendanceRequest(5L, true),
                new UserAttendanceRequest(6L, true),
                new UserAttendanceRequest(7L, true)
        ));

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .body(userAttendancesRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/meetings/" + id)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }
}
