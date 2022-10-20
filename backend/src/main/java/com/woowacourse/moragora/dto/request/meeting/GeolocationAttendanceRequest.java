package com.woowacourse.moragora.dto.request.meeting;

import com.woowacourse.moragora.domain.geolocation.Beacon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class GeolocationAttendanceRequest {

    private double latitude;
    private double longitude;

    public GeolocationAttendanceRequest(final Double latitude, final Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Beacon toEntity() {
        return Beacon.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
