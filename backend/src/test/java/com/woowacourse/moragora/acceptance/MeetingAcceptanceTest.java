package com.woowacourse.moragora.acceptance;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

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
}
