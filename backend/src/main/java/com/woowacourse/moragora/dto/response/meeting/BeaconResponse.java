package com.woowacourse.moragora.dto.response.meeting;

import com.woowacourse.moragora.domain.geolocation.Beacon;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BeaconResponse {

    private String address;

    /** 위도 */
    private Double latitude;

    /** 경도 */
    private Double longitude;

    /** 비콘의 반직경 :: 출석을 할 수 있는 범위를 나타냄 */
    private Integer radius;

    public BeaconResponse(final String address, final Double latitude, final Double longitude, final Integer radius) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public static BeaconResponse from(final Beacon beacon) {
        return new BeaconResponse(beacon.getAddress(), beacon.getLatitude(), beacon.getLongitude(), beacon.getRadius());
    }
}
