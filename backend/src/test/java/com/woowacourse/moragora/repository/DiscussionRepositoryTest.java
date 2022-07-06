package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.entity.Discussion;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
class DiscussionRepositoryTest {

    @Autowired
    private DiscussionRepository discussionRepository;

    @DisplayName("게시글을 저장한다.")
    @ParameterizedTest
    @ValueSource(strings = {"01234567890123456789012345678901234567890123456789",
            "영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구",
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij"})
    void save(final String title) {
        // given
        final Discussion discussion = new Discussion(title, "내용");

        // when
        final Discussion savedDiscussion = discussionRepository.save(discussion);

        // then
        assertThat(savedDiscussion).usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(new Discussion(title, "내용"));
    }

    @DisplayName("게시글을 저장할 때 제목의 길이가 50자를 초과할 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"012345678901234567890123456789012345678901234567891",
            "영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영",
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija"})
    void save_throwsException_ifTitleIsTooLong(final String title) {
        // given
        final Discussion discussion = new Discussion(title, "내용");

        // when, then
        assertThatThrownBy(() -> discussionRepository.save(discussion))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("모든 게시글을 조회한다.")
    @Test
    void findAll() {
        // given
        final Discussion discussion1 = new Discussion("제목1", "내용1");
        final Discussion discussion2 = new Discussion("제목2", "내용2");
        final Discussion discussion3 = new Discussion("제목3", "내용3");
        discussionRepository.save(discussion1);
        discussionRepository.save(discussion2);
        discussionRepository.save(discussion3);

        // when
        final List<Discussion> discussions = discussionRepository.findAll();

        // then
        assertThat(discussions).usingRecursiveComparison()
                .comparingOnlyFields("title", "content")
                .isEqualTo(List.of(discussion1, discussion2, discussion3));
    }
}
