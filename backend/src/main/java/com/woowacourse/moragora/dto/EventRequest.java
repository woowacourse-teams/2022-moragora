package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@Getter
@ToString
public class EventRequest {

    private static final String MISSING_REQUIRED_INPUT = "필수 입력 값이 누락됐습니다.";

    @NotNull(message = MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = "'T'HH:mm")
    private LocalTime entranceTime;

    @NotNull(message = MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = "'T'HH:mm")
    private LocalTime leaveTime;

    @NotNull(message = MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Builder
    public EventRequest(final LocalTime entranceTime, final LocalTime leaveTime, final LocalDate date) {
        this.entranceTime = entranceTime;
        this.leaveTime = leaveTime;
        this.date = date;
    }

    public Event toEntity(final Meeting meeting) {
        return new Event(date, entranceTime, leaveTime, meeting);
    }
}
