package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.DiscussionRequest;
import com.woowacourse.moragora.dto.OpinionRequest;
import com.woowacourse.moragora.exception.DiscussionNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class OpinionServiceTest {

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private DiscussionService discussionService;

    @DisplayName("답변을 저장한다.")
    @Test
    void save() {
        // given
        final DiscussionRequest discussionRequest = new DiscussionRequest("제목", "내용");
        final long discussionId = discussionService.save(discussionRequest);

        final OpinionRequest opinionRequest = new OpinionRequest("첫 번째 의견");

        // when
        final long expected = opinionService.save(discussionId, opinionRequest);

        // then
        assertThat(expected).isEqualTo(1L);
    }


    @DisplayName("존재하지 않는 게시글 id에 의견을 등록할 경우 예외가 발생한다.")
    @Test
    void save_throwsException_ifDiscussionNotFound() {
        // given
        final OpinionRequest opinionRequest = new OpinionRequest("첫 번째 의견");

        // when, then
        assertThatThrownBy(() -> opinionService.save(1L, opinionRequest))
                .isInstanceOf(DiscussionNotFoundException.class);
    }
}
