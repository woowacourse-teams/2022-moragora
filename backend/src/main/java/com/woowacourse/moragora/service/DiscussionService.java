package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.DiscussionRequest;
import com.woowacourse.moragora.dto.DiscussionResponse;
import com.woowacourse.moragora.dto.DiscussionsResponse;
import com.woowacourse.moragora.entity.Discussion;
import com.woowacourse.moragora.repository.DiscussionRepository;
import java.util.List;
import java.util.stream.Collectors;
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

    public DiscussionsResponse findAll() {
        final List<Discussion> discussions = discussionRepository.findAll();
        final List<DiscussionResponse> discussionResponses = discussions.stream()
                .map(DiscussionResponse::from)
                .collect(Collectors.toList());

        return new DiscussionsResponse(discussionResponses);
    }

    @Transactional
    public DiscussionResponse findById(final Long id) {
        final Discussion discussion = discussionRepository.findById(id).get();
        discussion.increaseViews();

        return DiscussionResponse.from(discussion);
    }
}
