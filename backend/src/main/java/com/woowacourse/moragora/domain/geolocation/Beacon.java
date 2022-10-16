package com.woowacourse.moragora.domain.geolocation;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import com.woowacourse.moragora.domain.meeting.Meeting;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "beacon")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Beacon {

    private static final double TO_RADIAN = Math.PI / 180.0;
    private static final double TO_DEGREE = 180.0 / Math.PI;
    public static final int EARTH_RADIUS = 6371;
    public static final int TO_KILO_METER = 1_000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private Long id;

    private String address;

    private Double latitude;

    private Double longitude;

    private Integer radius;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Builder
    public Beacon(final Long id,
                  final String address,
                  final Double latitude,
                  final Double longitude,
                  final Integer radius,
                  final Meeting meeting) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.meeting = meeting;
    }

    public Beacon(final String address, final Double latitude, final Double longitude, final Integer radius) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    /**
     * 좌표 계산할 때 타겟이 되는 포지션에 주로 이 생성자를 사용하게 될 것
     */
    public Beacon(final Double latitude, final Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void mapMeeting(final Meeting meeting) {
        this.meeting = meeting;
    }

    /**
     * 구면 좌표계 상의 두 지점 사이의 거리를 구합니다. (by Haversine Formula)
     * <p>
     * 거리 = 지구_반직경 * arc_hav(h)
     * <p>
     * Haversine을 풀어 놓은 공식을 사용한다
     */
    public double calculateDistance(final Beacon other) {

        final double thisLatCos = cos(this.latitude * TO_RADIAN);
        final double otherLatCos = cos(other.latitude * TO_RADIAN);

        final double dLat = abs(this.latitude - other.latitude) * TO_RADIAN;
        final double dLong = abs(this.longitude - other.longitude) * TO_RADIAN;

        final double latHav = pow(sin(dLat / 2), 2.0);
        final double longHav = pow(sin(dLong / 2), 2.0);

        final double sqrt = sqrt(latHav + longHav * thisLatCos * otherLatCos);
        final double distance = 2 * EARTH_RADIUS * sqrt * TO_KILO_METER;

        return distance;
    }

    public boolean isInRadius(final Beacon other) {
        final double distance = calculateDistance(other);

        return distance <= radius;
    }
}
