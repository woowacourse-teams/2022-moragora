package com.woowacourse.moragora.acceptance;

import static org.hamcrest.Matchers.equalTo;

import com.woowacourse.moragora.dto.UserAttendanceRequest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("모임 관련 기능")
public class MeetingAcceptanceTest extends AcceptanceTest {

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
                .body("meetingCount", equalTo(0));
    }

    @DisplayName("모임의 출석을 마감하면 총 모임 횟수와 결석한 참가자들의 결일을 증가시키고 상태코드 204을 반환한다.")
    @Test
    void endAttendance() {
        // given
        final int id = 1;
        final List<UserAttendanceRequest> userAttendanceRequests = List.of(
                new UserAttendanceRequest(1L, false),
                new UserAttendanceRequest(2L, false),
                new UserAttendanceRequest(3L, true),
                new UserAttendanceRequest(4L, true),
                new UserAttendanceRequest(5L, true),
                new UserAttendanceRequest(6L, true),
                new UserAttendanceRequest(7L, true)
        );

        // when, then
        RestAssured.given().log().all()
                .body(userAttendanceRequests)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/meetings/" + id)
                .then().log().all();
    }
}

