package com.woowacourse.moragora.dto.request.meeting;

import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeoLocationAttendanceFailResponse implements GeoLocationAttendanceResponse {

    private static final HttpStatus stats = HttpStatus.BAD_REQUEST;

    private List<BeaconResponse> beacons;

    public GeoLocationAttendanceFailResponse(final List<BeaconResponse> beacons) {
        this.beacons = beacons;
    }
}
