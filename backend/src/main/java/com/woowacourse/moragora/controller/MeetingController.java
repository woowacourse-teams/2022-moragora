package com.woowacourse.moragora.controller;

import com.woowacourse.auth.support.Login;
import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.UserAttendancesRequest;
import com.woowacourse.moragora.service.MeetingService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(final MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    @Login
    public ResponseEntity<Void> add(@RequestBody @Valid final MeetingRequest request) {
        final Long id = meetingService.save(request);
        return ResponseEntity.created(URI.create("/meetings/" + id)).build();
    }

    @GetMapping("/{id}")
    @Login
    public ResponseEntity<MeetingResponse> findOne(@PathVariable final Long id) {
        final MeetingResponse meetingResponse = meetingService.findById(id);
        return ResponseEntity.ok(meetingResponse);
    }

    @PatchMapping("/{id}")
    @Login
    public ResponseEntity<Void> endAttendance(@PathVariable final Long id,
                                              @RequestBody final UserAttendancesRequest request) {
        meetingService.updateAttendance(id, request);
        return ResponseEntity.noContent().build();
    }
}
