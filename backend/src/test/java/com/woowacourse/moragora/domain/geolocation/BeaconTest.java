package com.woowacourse.moragora.domain.geolocation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeaconTest {

    @DisplayName("위도, 경도를 받아서 테스트한다")
    @Test
    void beacon_calculateDistance() {
        /**
         * 선릉역
         *  - 위도 37.50450727610866
         *  - 경도 127.04898205784248
         *
         * 잠실역
         *  - 위도 37.51432488124543
         *  - 경도 127.1031461188557
         */
        final Beacon beacon1 = new Beacon("선릉역", 37.50450727610866, 127.04898205784248, 100);
        final Beacon beacon2 = new Beacon("잠실역", 37.51432488124543, 127.1031461188557, 50);
        final double distance = beacon1.calculateDistance(beacon2);

        Assertions.assertThat(distance > 1_000).isTrue();
    }
}
