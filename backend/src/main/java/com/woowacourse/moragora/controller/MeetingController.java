package com.woowacourse.moragora.controller;

import com.woowacourse.auth.support.Authentication;
import com.woowacourse.auth.support.AuthenticationPrincipal;
import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingsResponse;
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
    public ResponseEntity<Void> add(@RequestBody @Valid final MeetingRequest request,
                                    @AuthenticationPrincipal final Long loginId) {
        final Long id = meetingService.save(request, loginId);
        return ResponseEntity.created(URI.create("/meetings/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponse> findOne(@PathVariable final Long id) {
        final MeetingResponse meetingResponse = meetingService.findById(id);
        return ResponseEntity.ok(meetingResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<MyMeetingsResponse> findMy(@AuthenticationPrincipal final Long id) {
        final MyMeetingsResponse meetingsResponse = meetingService.findAllByUserId(id);
        return ResponseEntity.ok(meetingsResponse);
    }

    @PutMapping("/{meetingId}/users/{userId}")
    public ResponseEntity<UserAttendanceRequest> checkAttendance(@PathVariable final Long meetingId,
                                                                 @PathVariable final Long userId,
                                                                 @RequestBody final UserAttendanceRequest request,
                                                                 @AuthenticationPrincipal final Long loginId) {
        meetingService.updateAttendance(meetingId, userId, request, loginId);
        return ResponseEntity.noContent().build();
    }
}
