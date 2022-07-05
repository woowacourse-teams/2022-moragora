package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Discussion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DiscussionRepositoryTest {

    @Autowired
    private DiscussionRepository discussionRepository;

    @DisplayName("게시글을 저장한다.")
    @Test
    void save() {
        // given
        final Discussion discussion = new Discussion("제목", "내용");

        // when
        final Discussion savedDiscussion = discussionRepository.save(discussion);

        // then
        assertThat(savedDiscussion).usingRecursiveComparison()
                .isEqualTo(new Discussion(1L, "제목", "내용"));
    }
}
