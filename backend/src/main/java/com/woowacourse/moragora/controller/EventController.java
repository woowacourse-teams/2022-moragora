package com.woowacourse.moragora.controller;

import com.woowacourse.auth.support.Authentication;
import com.woowacourse.auth.support.AuthenticationPrincipal;
import com.woowacourse.auth.support.MasterAuthorization;
import com.woowacourse.moragora.dto.EventCancelRequest;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.service.EventService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meetings/{meetingId}/events")
@Authentication
public class EventController {

    private final EventService eventService;

    public EventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @MasterAuthorization
    @PostMapping
    public ResponseEntity<Void> add(@PathVariable final Long meetingId,
                                    @RequestBody @Valid final EventsRequest request,
                                    @AuthenticationPrincipal final Long loginId) {
        eventService.save(request, meetingId);
        return ResponseEntity.noContent().build();
    }

    @MasterAuthorization
    @DeleteMapping
    public ResponseEntity<Void> cancel(@PathVariable final Long meetingId,
                                       @RequestBody @Valid final EventCancelRequest request,
                                       @AuthenticationPrincipal final Long loginId) {
        eventService.cancel(request, meetingId);
        return ResponseEntity.noContent().build();
    }
}
