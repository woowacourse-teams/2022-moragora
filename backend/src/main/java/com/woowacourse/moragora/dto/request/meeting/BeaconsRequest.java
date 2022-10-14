package com.woowacourse.moragora.dto.request.meeting;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@NoArgsConstructor
@ToString
@Getter
public class BeaconsRequest {

    private List<BeaconRequest> geoLocationMeetingsRequest;

    public BeaconsRequest(final List<BeaconRequest> geoLocationMeetingsRequest) {
        this.geoLocationMeetingsRequest = geoLocationMeetingsRequest;
    }
}
