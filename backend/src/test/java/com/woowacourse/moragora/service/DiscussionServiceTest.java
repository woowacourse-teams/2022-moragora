package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.dto.DiscussionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DiscussionServiceTest {

    @Autowired
    private DiscussionService discussionService;

    @DisplayName("게시글을 저장한다.")
    @Test
    void save() {
        // given
        final DiscussionRequest discussionRequest = new DiscussionRequest("제목", "내용");

        // when
        final long expected = discussionService.save(discussionRequest);

        // then
        assertThat(expected).isEqualTo(1L);
    }
}
