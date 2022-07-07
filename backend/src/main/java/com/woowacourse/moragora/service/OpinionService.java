package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.OpinionRequest;
import com.woowacourse.moragora.entity.Discussion;
import com.woowacourse.moragora.entity.Opinion;
import com.woowacourse.moragora.exception.DiscussionNotFoundException;
import com.woowacourse.moragora.repository.DiscussionRepository;
import com.woowacourse.moragora.repository.OpinionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OpinionService {

    private final DiscussionRepository discussionRepository;
    private final OpinionRepository opinionRepository;

    public OpinionService(final DiscussionRepository discussionRepository,
                          final OpinionRepository opinionRepository) {
        this.discussionRepository = discussionRepository;
        this.opinionRepository = opinionRepository;
    }


    public long save(final Long discussionId, final OpinionRequest request) {
        final Discussion discussion = discussionRepository.findById(discussionId)
                .orElseThrow(DiscussionNotFoundException::new);

        final Opinion opinion = new Opinion(request.getContent(), discussion);
        final Opinion savedOpinion = opinionRepository.save(opinion);

        return savedOpinion.getId();
    }
}
