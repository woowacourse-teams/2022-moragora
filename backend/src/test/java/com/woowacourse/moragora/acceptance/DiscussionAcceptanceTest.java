package com.woowacourse.moragora.acceptance;

import com.woowacourse.moragora.dto.DiscussionRequest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("게시글 관련 기능")
public class DiscussionAcceptanceTest extends AcceptanceTest {

    @DisplayName("사용자가 토론 게시글을 등록하면 등록이 완료되고 상태코드 201을 반환한다.")
    @Test
    void createDiscussion() {
        // given
        final DiscussionRequest discussionRequest = new DiscussionRequest("제목", "내용");

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .body(discussionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/discussions")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }
}
