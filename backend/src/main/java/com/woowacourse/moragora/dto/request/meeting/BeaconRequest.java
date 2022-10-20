package com.woowacourse.moragora.dto.request.meeting;

import static com.woowacourse.moragora.constant.ValidationMessages.MISSING_REQUIRED_INPUT;

import com.woowacourse.moragora.domain.geolocation.Beacon;
import com.woowacourse.moragora.domain.meeting.Meeting;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@Getter
@ToString
public class BeaconRequest {

    private static final int MAX_ADDRESS_LENGTH = 50;

    @NotBlank(message = MISSING_REQUIRED_INPUT)
    @Length(max = MAX_ADDRESS_LENGTH, message = "비콘 주소는 " + MAX_ADDRESS_LENGTH + "자를 초과할 수 없습니다.")
    private String address;

    @Range(max = 90, min = -90, message = "위도는 +90(북위)에서 -90(남위)사이의 숫자를 넘길 수 없습니다")
    private Double latitude;

    @Range(max = 180, min = -180, message = "경도는 +180(서경)에서 -180(동경)사이의 숫자를 넘길 수 없습니다")
    private Double longitude;

    @Min(value = 50, message = "비콘의 반경은 최소 " + MAX_ADDRESS_LENGTH + "m 이상이어야 합니다.")
    private Integer radius;

    @Builder
    public BeaconRequest(final String address, final Double latitude, final Double longitude, final Integer radius) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public Beacon toEntity(final Meeting meeting) {
        return Beacon.builder()
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .radius(radius)
                .meeting(meeting)
                .build();
    }
}
