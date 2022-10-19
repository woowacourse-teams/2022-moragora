package com.woowacourse.moragora.dto.response.meeting;

import com.woowacourse.moragora.domain.geolocation.Beacon;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class BeaconsResponse {

    private List<BeaconResponse> beacons;

    public BeaconsResponse(final List<Beacon> beacons) {
        this.beacons = beacons.stream()
                .map(BeaconResponse::from)
                .collect(Collectors.toList());
    }
}
