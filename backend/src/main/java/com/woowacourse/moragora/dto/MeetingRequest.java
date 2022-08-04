package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@Getter
public class MeetingRequest {

    private static final int MAX_NAME_LENGTH = 50;
    private static final String MISSING_REQUIRED_INPUT = "필수 입력 값이 누락됐습니다.";

    @NotBlank(message = MISSING_REQUIRED_INPUT)
    @Length(max = MAX_NAME_LENGTH, message = "모임 이름은 " + MAX_NAME_LENGTH + "자를 초과할 수 없습니다.")
    private String name;

    @NotNull(message = MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = "'T'HH:mm")
    private LocalTime entranceTime;

    @NotNull(message = MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = "'T'HH:mm")
    private LocalTime leaveTime;

    private List<Long> userIds;

    @Builder
    public MeetingRequest(final String name,
                          final LocalDate startDate,
                          final LocalDate endDate,
                          final LocalTime entranceTime,
                          final LocalTime leaveTime,
                          final List<Long> userIds) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entranceTime = entranceTime;
        this.leaveTime = leaveTime;
        this.userIds = userIds;
    }

    public Meeting toEntity() {
        return new Meeting(name, startDate, endDate);
    }
}
