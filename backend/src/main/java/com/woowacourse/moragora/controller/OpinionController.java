package com.woowacourse.moragora.controller;

import com.woowacourse.moragora.dto.OpinionRequest;
import com.woowacourse.moragora.service.OpinionService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discussions/{discussionId}/opinions")
public class OpinionController {

    private final OpinionService opinionService;

    public OpinionController(final OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@PathVariable final Long discussionId, @RequestBody final OpinionRequest request) {
        final long opinionId = opinionService.save(discussionId, request);
        return ResponseEntity.created(URI.create("/discussions/" + discussionId + "/opinions/" + opinionId)).build();
    }
}
