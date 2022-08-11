package com.woowacourse.moragora.controller;

import com.woowacourse.auth.support.Authentication;
import com.woowacourse.auth.support.AuthenticationPrincipal;
import com.woowacourse.auth.support.MasterAuthorization;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.dto.EventsResponse;
import com.woowacourse.moragora.service.EventService;
import java.time.LocalDate;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Void> add(@RequestBody @Valid final EventsRequest request,
                                    @PathVariable final Long meetingId,
                                    @AuthenticationPrincipal final Long loginId) {
        eventService.save(request, meetingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<EventsResponse> inquireByDuration(@PathVariable final Long meetingId,
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                            @RequestParam(value = "begin", required = false) final LocalDate begin,
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                            @RequestParam(value = "end", required = false) final LocalDate end) {
        EventsResponse eventsResponse = eventService.inquireByDuration(meetingId, begin, end);
        return ResponseEntity.ok(eventsResponse);
    }
}
