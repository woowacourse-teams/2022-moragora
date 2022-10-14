package com.woowacourse.moragora.domain.geolocation;

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

    public boolean isInRadius(final Beacon other) {
        final double distance = calculateDistance(other);

        return distance <= radius;
    }

    public double calculateDistance(final Beacon other) {
        double theta = this.latitude - other.latitude;

        double dist =
                Math.sin(deg2rad(this.latitude)) * Math.sin(deg2rad(other.latitude)) +
                        Math.cos(deg2rad(this.latitude)) * Math.cos(deg2rad(other.latitude)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist *= (60 * 1.1515);
        dist *= 1609.344;

        return dist;
    }

    private double deg2rad(final double deg) {
        return deg * Math.PI / 180;
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
