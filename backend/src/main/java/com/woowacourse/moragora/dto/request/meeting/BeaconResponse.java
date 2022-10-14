package com.woowacourse.moragora.dto.request.meeting;

import com.woowacourse.moragora.domain.geolocation.Beacon;
import lombok.Getter;

@Getter
public class BeaconResponse {

    private Long id;
    private String address;
    private int distance;

    public BeaconResponse(final Beacon beacon, final double distance) {
        this.id = beacon.getId();
        this.address = beacon.getAddress();
        this.distance = (int) distance;
    }
}
