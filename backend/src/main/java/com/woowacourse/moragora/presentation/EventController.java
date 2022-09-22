package com.woowacourse.moragora.presentation;

import com.woowacourse.moragora.application.EventService;
import com.woowacourse.moragora.application.auth.MasterAuthorization;
import com.woowacourse.moragora.dto.request.event.EventCancelRequest;
import com.woowacourse.moragora.dto.request.event.EventsRequest;
import com.woowacourse.moragora.dto.response.event.EventResponse;
import com.woowacourse.moragora.dto.response.event.EventsResponse;
import com.woowacourse.moragora.presentation.auth.Authentication;
import com.woowacourse.moragora.presentation.auth.AuthenticationPrincipal;
import java.time.LocalDate;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @GetMapping("/upcoming")
    public ResponseEntity<EventResponse> showUpcomingEvent(@PathVariable final Long meetingId,
                                                           @AuthenticationPrincipal final Long loginId) {
        final EventResponse eventResponse = eventService.findUpcomingEvent(meetingId);
        return ResponseEntity.ok(eventResponse);
    }

    @GetMapping
    public ResponseEntity<EventsResponse> showByDuration(@PathVariable final Long meetingId,
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                         @RequestParam(value = "begin", required = false) final LocalDate begin,
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                         @RequestParam(value = "end", required = false) final LocalDate end) {
        EventsResponse eventsResponse = eventService.findByDuration(meetingId, begin, end);
        return ResponseEntity.ok(eventsResponse);
    }
}
