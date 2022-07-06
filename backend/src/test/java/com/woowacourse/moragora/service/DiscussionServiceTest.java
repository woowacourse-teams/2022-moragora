package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.dto.DiscussionRequest;
import com.woowacourse.moragora.dto.DiscussionResponse;
import com.woowacourse.moragora.dto.DiscussionsResponse;
import java.util.List;
import java.util.stream.Collectors;
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

    @DisplayName("게시글 리스트를 조회한다.")
    @Test
    void findAll() {
        // given
        final DiscussionRequest discussionRequest1 = new DiscussionRequest("제목1", "내용1");
        final DiscussionRequest discussionRequest2 = new DiscussionRequest("제목2", "내용2");

        final long expected1 = discussionService.save(discussionRequest1);
        final long expected2 = discussionService.save(discussionRequest2);

        // when
        final DiscussionsResponse response = discussionService.findAll();
        final List<Long> foundIds = response.getDiscussions()
                .stream()
                .map(DiscussionResponse::getId)
                .collect(Collectors.toList());

        // then
        assertThat(foundIds).containsExactly(expected1, expected2);
    }
}