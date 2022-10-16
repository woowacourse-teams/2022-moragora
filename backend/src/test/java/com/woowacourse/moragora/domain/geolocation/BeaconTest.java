package com.woowacourse.moragora.domain.geolocation;

import static java.lang.Math.sqrt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.data.Percentage.withPercentage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BeaconTest {

    private static final Logger log = LoggerFactory.getLogger(BeaconTest.class);

    @DisplayName("위도 사이의 1도는 111km에 근접한다")
    @Test
    void beacon_calculateDistance_case_4() {
        final Beacon beacon1 = new Beacon("A", 36.0, 127.0, 100);
        final Beacon beacon2 = new Beacon("B", 37.0, 127.0, 50);
        final double distance = beacon1.calculateDistance(beacon2) / 1_000.0;

        assertThat(distance).isCloseTo(111.0, offset(1.0));
    }

    @DisplayName("적도에서 경도 사이의 1도는 111km에 근접한다")
    @Test
    void beacon_calculateDistance_case_7() {
        final Beacon beacon1 = new Beacon("A", 0.0, 127.0, 100);
        final Beacon beacon2 = new Beacon("B", 0.0, 128.0, 50);
        final double distance = beacon1.calculateDistance(beacon2) / 1_000.0;

        assertThat(distance).isCloseTo(111.0, offset(1.0));
    }

    @DisplayName("북반구에서 경도 사이의 1도는 0m에 수렴한다")
    @Test
    void beacon_calculateDistance_case_8() {
        final Beacon beacon1 = new Beacon("A", 90.0, 127.0, 100);
        final Beacon beacon2 = new Beacon("B", 90.0, 128.0, 50);
        final double distance = beacon1.calculateDistance(beacon2);

        assertThat(distance).isCloseTo(0.0, offset(1E-11));
    }

    @DisplayName("적도에서 위도와 경도가 각각 1도 차이가 날 경우, 평면상 두 점 사이 거리 계산과 1% 차이로 근접하다")
    @Test
    void beacon_calculateDistance_case_81() {
        final Beacon beacon1 = new Beacon("A", 0.0, 127.0, 50);
        final Beacon beacon2 = new Beacon("B", 1.0, 128.0, 50);
        final double distanceOnEarth = beacon1.calculateDistance(beacon2) / 1_000.0;

        logDistance(distanceOnEarth);

        final Beacon equatorLocation1 = new Beacon("", 0.0, 0.0, 50);
        final Beacon equatorLocation2 = new Beacon("", 0.0, 1.0, 50);
        final double equatorDistance = equatorLocation1.calculateDistance(equatorLocation2) / 1_000.0;
        double distanceOnPlain = sqrt(2) * equatorDistance;

        assertThat(distanceOnEarth).isCloseTo(distanceOnPlain, withPercentage(1));
    }

    @DisplayName("대한민국의 북위 37도에서 경도 사이의 1도는 881km에 근접한다")
    @Test
    void beacon_calculateDistance_case_5() {
        final Beacon beacon1 = new Beacon("A", 37.0, 127.0, 100);
        final Beacon beacon2 = new Beacon("B", 37.0, 128.0, 50);
        final double distance = beacon1.calculateDistance(beacon2) / 1_000.0;

        assertThat(distance).isCloseTo(88.0, offset(1.0));
    }

    @ParameterizedTest
    @CsvSource(delimiterString = " | ",
            value = {
                    "선릉역 | 37.50450 | 127.048982 | 잠실역 | 37.5132 | 127.10013 | 4620",
                    "서울역 | 37.54788 | 126.99712 | 부산역 | 35.15887 | 129.04384 | 328000",
                    "잠실역 8번 출구 | 37.5138 | 127.1012 | 루터회관 | 37.5153 | 127.103 | 230.0"
            })
    void beacon_calculateDistance_case_real_world(
            final String address1, final double latitude1, final double longitude1,
            final String address2, final double latitude2, final double longitude2,
            final double expectedDistance) {
        final Beacon beacon1 = new Beacon(address1, latitude1, longitude1, 50);
        final Beacon beacon2 = new Beacon(address2, latitude2, longitude2, 50);
        final double distance = beacon1.calculateDistance(beacon2);

        logDistance(distance);

        assertThat(distance).isCloseTo(expectedDistance, withPercentage(2));
    }

    private void logDistance(final double distance) {
        log.info(":: [distance] {}", distance);
    }
}
