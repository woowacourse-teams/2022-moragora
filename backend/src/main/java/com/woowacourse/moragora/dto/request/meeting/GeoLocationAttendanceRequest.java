package com.woowacourse.moragora.dto.request.meeting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class GeoLocationAttendanceRequest {

    private Double latitude;
    private Double longitude;

    public GeoLocationAttendanceRequest(final Double latitude, final Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
