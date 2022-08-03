package com.woowacourse.moragora.controller;

import com.woowacourse.auth.support.Authentication;
import com.woowacourse.auth.support.AuthenticationPrincipal;
import com.woowacourse.auth.support.MasterAuthorization;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Authentication
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(final AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @MasterAuthorization
    @PutMapping("/meetings/{meetingId}/users/{userId}")
    public ResponseEntity<Void> markAttendance(@PathVariable final Long meetingId,
                                               @PathVariable final Long userId,
                                               @RequestBody final UserAttendanceRequest request,
                                               @AuthenticationPrincipal final Long loginId) {
        attendanceService.updateAttendance(meetingId, userId, request);
        return ResponseEntity.noContent().build();
    }

    @MasterAuthorization
    @PostMapping("/meetings/{meetingId}/coffees/use")
    public ResponseEntity<Void> useCoffeeStack(@PathVariable final Long meetingId,
                                               @AuthenticationPrincipal final Long loginId) {
        attendanceService.disableUsedTardy(meetingId);
        return ResponseEntity.noContent().build();
    }

}
