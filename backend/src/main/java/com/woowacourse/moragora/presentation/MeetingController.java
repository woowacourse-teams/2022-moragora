package com.woowacourse.moragora.presentation;

import com.woowacourse.moragora.application.MeetingService;
import com.woowacourse.moragora.application.auth.MasterAuthorization;
import com.woowacourse.moragora.dto.request.meeting.BeaconRequest;
import com.woowacourse.moragora.dto.request.meeting.GeoLocationAttendanceRequest;
import com.woowacourse.moragora.dto.request.meeting.GeoLocationAttendanceResponse;
import com.woowacourse.moragora.dto.request.meeting.GeoLocationAttendanceSuccessResponse;
import com.woowacourse.moragora.dto.request.meeting.MasterRequest;
import com.woowacourse.moragora.dto.request.meeting.MeetingRequest;
import com.woowacourse.moragora.dto.request.meeting.MeetingUpdateRequest;
import com.woowacourse.moragora.dto.response.meeting.MeetingResponse;
import com.woowacourse.moragora.dto.response.meeting.MyMeetingsResponse;
import com.woowacourse.moragora.presentation.auth.Authentication;
import com.woowacourse.moragora.presentation.auth.AuthenticationPrincipal;
import com.woowacourse.moragora.support.ValidList;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
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
    public ResponseEntity<MeetingResponse> findOne(@PathVariable final Long id, @AuthenticationPrincipal Long loginId) {
        final MeetingResponse meetingResponse = meetingService.findById(id, loginId);
        return ResponseEntity.ok(meetingResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<MyMeetingsResponse> findMy(@AuthenticationPrincipal final Long loginId) {
        final MyMeetingsResponse meetingsResponse = meetingService.findAllByUserId(loginId);
        return ResponseEntity.ok(meetingsResponse);
    }

    @MasterAuthorization
    @PutMapping("/{meetingId}/master")
    public ResponseEntity<Void> passMaster(@PathVariable final Long meetingId,
                                           @RequestBody final MasterRequest masterRequest,
                                           @AuthenticationPrincipal final Long loginId) {
        meetingService.assignMaster(meetingId, masterRequest, loginId);
        return ResponseEntity.noContent().build();
    }

    @MasterAuthorization
    @PutMapping("/{meetingId}")
    public ResponseEntity<MeetingUpdateRequest> changeName(@PathVariable final Long meetingId,
                                                           @RequestBody @Valid MeetingUpdateRequest request,
                                                           @AuthenticationPrincipal final Long loginId) {
        meetingService.updateName(request, meetingId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{meetingId}/me")
    public ResponseEntity<Void> deleteMeFrom(@PathVariable final Long meetingId,
                                             @AuthenticationPrincipal Long loginId) {
        meetingService.deleteParticipant(meetingId, loginId);
        return ResponseEntity.noContent().build();
    }

    @MasterAuthorization
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<Void> remove(@PathVariable final Long meetingId,
                                       @AuthenticationPrincipal final Long loginId) {
        meetingService.deleteMeeting(meetingId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 위치 기반 :: 비콘 생성
     */
    @PostMapping("/{meetingId}/beacons")
    public ResponseEntity<Void> addBeacons(@PathVariable final Long meetingId,
                                           @RequestBody @Valid final ValidList<BeaconRequest> requestBody) {
        meetingService.saveBeacons(meetingId, requestBody);
        return ResponseEntity.noContent().build();
    }

    /**
     * 위치 기반 :: 출석 체크
     */
    @PostMapping("/{meetingId}/users/{userId}/attendances/today/geolocation")
    public ResponseEntity<GeoLocationAttendanceResponse> attendWithBeaconBase(@PathVariable final Long meetingId,
                                                                              @PathVariable final Long userId,
                                                                              @RequestBody final GeoLocationAttendanceRequest requestBody) {
        final GeoLocationAttendanceResponse responseBody =
                meetingService.attendWithGeoLocation(meetingId, userId, requestBody);

        if (responseBody instanceof GeoLocationAttendanceSuccessResponse) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body(responseBody);
    }
}
