package com.woowacourse.moragora.dto.request.meeting;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class BeaconsRequest {

    @Valid
    private List<BeaconRequest> beacons = new ArrayList<>();

    public BeaconsRequest(final List<BeaconRequest> beacons) {
        this.beacons = beacons;
    }
}
