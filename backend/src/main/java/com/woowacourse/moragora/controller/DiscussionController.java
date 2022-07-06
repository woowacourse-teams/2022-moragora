package com.woowacourse.moragora.controller;

import com.woowacourse.moragora.dto.DiscussionRequest;
import com.woowacourse.moragora.dto.DiscussionsResponse;
import com.woowacourse.moragora.service.DiscussionService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discussions")
public class DiscussionController {

    private final DiscussionService discussionService;

    public DiscussionController(final DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody @Valid final DiscussionRequest request) {
        final long id = discussionService.save(request);
        return ResponseEntity.created(URI.create("/discussions/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<DiscussionsResponse> findAll() {
        final DiscussionsResponse discussionsResponse = discussionService.findAll();
        return ResponseEntity.ok(discussionsResponse);
    }
}
