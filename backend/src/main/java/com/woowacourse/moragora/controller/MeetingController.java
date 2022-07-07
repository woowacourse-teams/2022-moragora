package com.woowacourse.moragora.controller;

import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.service.MeetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(final MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponse> findOne(@PathVariable final Long id) {
        final MeetingResponse meetingResponse = meetingService.findById(id);
        return ResponseEntity.ok(meetingResponse);
    }
}
