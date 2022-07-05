package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.DiscussionRequest;
import com.woowacourse.moragora.entity.Discussion;
import com.woowacourse.moragora.repository.DiscussionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DiscussionService {

    private final DiscussionRepository discussionRepository;

    public DiscussionService(final DiscussionRepository discussionRepository) {
        this.discussionRepository = discussionRepository;
    }

    @Transactional
    public long save(final DiscussionRequest discussionRequest) {
        final Discussion discussion = new Discussion(discussionRequest.getTitle(), discussionRequest.getContent());
        final Discussion savedDiscussion = discussionRepository.save(discussion);

        return savedDiscussion.getId();
    }
}
