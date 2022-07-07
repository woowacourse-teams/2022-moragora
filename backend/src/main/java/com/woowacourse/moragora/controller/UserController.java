package com.woowacourse.moragora.controller;

import com.woowacourse.moragora.dto.UserResponse;
import com.woowacourse.moragora.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meetings/{meetingId}/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findByMeeting(@PathVariable final Long meetingId) {
        final List<UserResponse> userResponses = userService.findByMeetingId(meetingId);
        return ResponseEntity.ok(userResponses);
    }
}
