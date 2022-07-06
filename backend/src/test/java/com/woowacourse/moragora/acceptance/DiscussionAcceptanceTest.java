package com.woowacourse.moragora.acceptance;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

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
        ValidatableResponse response = post("/discussions", discussionRequest);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }

    @DisplayName("사용자가 토론 목록을 조회하면 모든 토론 게시글과 상태코드 200을 반환한다.")
    @Test
    void findAllDiscussions() {
        // given
        final DiscussionRequest discussionRequest1 = new DiscussionRequest("제목1", "내용1");
        final DiscussionRequest discussionRequest2 = new DiscussionRequest("제목2", "내용2");
        final DiscussionRequest discussionRequest3 = new DiscussionRequest("제목3", "내용3");
        post("/discussions", discussionRequest1);
        post("/discussions", discussionRequest2);
        post("/discussions", discussionRequest3);

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/discussions")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("discussions.id", contains(1, 2, 3))
                .body("discussions.title", contains("제목1", "제목2", "제목3"))
                .body("discussions.content", contains("내용1", "내용2", "내용3"));
    }

    @DisplayName("사용자가 특정 토론을 조회하면 해당 토론 게시글과 상태코드 200을 반환한다.")
    @Test
    void show() {
        // given
        final DiscussionRequest discussionRequest = new DiscussionRequest("제목", "내용");
        final ValidatableResponse createResponse = post("/discussions", discussionRequest);
        final String uri = createResponse.extract()
                .header("Location");
        final int id = Integer.parseInt(uri.split("/discussions/")[1]);

        // when
        final ValidatableResponse response = get(uri);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", equalTo(id))
                .body("title", equalTo("제목"))
                .body("content", equalTo("내용"));
    }
}
