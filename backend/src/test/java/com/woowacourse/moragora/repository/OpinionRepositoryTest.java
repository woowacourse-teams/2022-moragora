package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.Discussion;
import com.woowacourse.moragora.entity.Opinion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class OpinionRepositoryTest {

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private OpinionRepository opinionRepository;

    @DisplayName("의견을 저장한다.")
    @Test
    void save() {
        // given
        final Discussion discussion = new Discussion("제목", "내용");
        final Discussion savedDiscussion = discussionRepository.save(discussion);
        final Opinion opinion = new Opinion("첫 번째 의견", savedDiscussion);

        // when
        final Opinion savedOpinion = opinionRepository.save(opinion);

        // then
        assertThat(savedOpinion).usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(new Opinion("첫 번째 의견", savedDiscussion));
    }
}
