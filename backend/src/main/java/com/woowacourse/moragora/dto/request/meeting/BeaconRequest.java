package com.woowacourse.moragora.dto.request.meeting;

import com.woowacourse.moragora.domain.geolocation.Beacon;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class BeaconRequest {

    private String address;
    private Double latitude;
    private Double longitude;
    private Integer radius;

    @Builder
    public BeaconRequest(final String address, final Double latitude, final Double longitude, final Integer radius) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public Beacon toEntity() {
        return Beacon.builder()
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .radius(radius)
                .build();
    }
}
