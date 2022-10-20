package com.woowacourse.moragora.dto.request.meeting;

import com.woowacourse.moragora.domain.geolocation.Beacon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@ToString
@Getter
public class GeolocationAttendanceRequest {

    @Range(max = 90, min = -90, message = "위도는 +90(북위)에서 -90(남위)사이의 숫자를 넘길 수 없습니다")
    private double latitude;

    @Range(max = 180, min = -180, message = "경도는 +180(서경)에서 -180(동경)사이의 숫자를 넘길 수 없습니다")
    private double longitude;

    public GeolocationAttendanceRequest(final double latitude, final double longitude) {
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
