package com.woowacourse.moragora.dto.request.meeting;

import com.woowacourse.moragora.domain.geolocation.Beacon;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
@ToString
public class BeaconRequest {

    private static final int MAX_ADDRESS_LENGTH = 50;
    private static final String MISSING_REQUIRED_INPUT = "필수 입력 값이 누락됐습니다.";

    @NotBlank(message = MISSING_REQUIRED_INPUT)
    @Length(max = MAX_ADDRESS_LENGTH, message = "비콘 주소는 " + MAX_ADDRESS_LENGTH + "자를 초과할 수 없습니다.")
    private String address;

    private Double latitude;

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

    public Beacon toEntity() {
        return Beacon.builder()
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .radius(radius)
                .build();
    }
}
