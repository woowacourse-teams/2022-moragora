package com.woowacourse.moragora.controller;

import com.woowacourse.auth.support.Authentication;
import com.woowacourse.auth.support.AuthenticationPrincipal;
import com.woowacourse.auth.support.MasterAuthorization;
import com.woowacourse.moragora.dto.EventResponse;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.service.EventService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Authentication
public class EventController {

    private final EventService eventService;

    public EventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @MasterAuthorization
    @PostMapping("/meetings/{meetingId}/events")
    public ResponseEntity<Void> add(@RequestBody @Valid final EventsRequest request,
                                    @PathVariable final Long meetingId,
                                    @AuthenticationPrincipal final Long loginId) {
        eventService.save(request, meetingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/meetings/{meetingId}/events/upcoming")
    public ResponseEntity<EventResponse> showUpcomingEvent(@PathVariable final Long meetingId,
                                                           @AuthenticationPrincipal final Long loginId) {
        final EventResponse eventResponse = eventService.findUpcomingEvent(meetingId);
        return ResponseEntity.ok(eventResponse);
    }
}
