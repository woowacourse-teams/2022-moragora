package com.woowacourse.moragora.acceptance;

import com.woowacourse.moragora.dto.DiscussionRequest;
import com.woowacourse.moragora.dto.OpinionRequest;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("답변 관련 기능")
public class OpinionAcceptanceTest extends AcceptanceTest {

    @DisplayName("사용자가 토론 게시글에 의견을 등록하면 등록이 완료되고 상태코드 201을 반환한다.")
    @Test
    void createDiscussion() {
        // given
        final DiscussionRequest discussionRequest = new DiscussionRequest("제목", "내용");
        final ValidatableResponse discussionCreateResponse = post("/discussions", discussionRequest);
        final String uri = discussionCreateResponse.extract().header("Location");
        final OpinionRequest opinionRequest = new OpinionRequest("첫 번째 의견");

        // when
        final ValidatableResponse opinionCreateResponse = post(uri + "/opinions", opinionRequest);

        // then
        opinionCreateResponse.statusCode(HttpStatus.CREATED.value()).header("Location", Matchers.notNullValue());
    }
}
