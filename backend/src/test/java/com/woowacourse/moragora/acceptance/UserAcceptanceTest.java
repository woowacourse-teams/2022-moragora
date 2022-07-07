package com.woowacourse.moragora.acceptance;

import static org.hamcrest.Matchers.contains;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class UserAcceptanceTest extends AcceptanceTest {

    @DisplayName("사용자가 특정 모임을 조회하면 해당 모임의 모든 참가자 정보와 상태코드 200을 반환한다.")
    @Test
    void findByMeeting() {
        // given
        final int meetingId = 1;

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/users");

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", contains(1, 2, 3, 4, 5, 6, 7))
                .body("name", contains("아스피", "필즈", "포키", "썬", "우디", "쿤", "반듯"))
                .body("absentCount", contains(0, 0, 0, 0, 0, 0, 0));
    }
}
