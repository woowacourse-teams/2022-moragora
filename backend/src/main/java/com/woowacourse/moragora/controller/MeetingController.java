package com.woowacourse.moragora.controller;

import com.woowacourse.auth.support.Authentication;
import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.service.MeetingService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meetings")
@Authentication
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(final MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody @Valid final MeetingRequest request) {
        final Long id = meetingService.save(request, 1L);
        return ResponseEntity.created(URI.create("/meetings/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponse> findOne(@PathVariable final Long id) {
        final MeetingResponse meetingResponse = meetingService.findById(id, 1L);
        return ResponseEntity.ok(meetingResponse);
    }

    @PutMapping("/{meetingId}/users/{userId}")
    public ResponseEntity<UserAttendanceRequest> endAttendance(@PathVariable final Long meetingId,
                                                               @PathVariable final Long userId,
                                                               @RequestBody final UserAttendanceRequest request) {
        meetingService.updateAttendance(meetingId, userId, request);
        return ResponseEntity.noContent().build();
    }
}
