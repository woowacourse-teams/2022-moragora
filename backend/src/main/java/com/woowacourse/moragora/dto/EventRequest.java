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
    private LocalTime meetingStartTime;

    @NotNull(message = MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = "'T'HH:mm")
    private LocalTime meetingEndTime;

    @NotNull(message = MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Builder
    public EventRequest(final LocalTime meetingStartTime, final LocalTime meetingEndTime, final LocalDate date) {
        this.meetingStartTime = meetingStartTime;
        this.meetingEndTime = meetingEndTime;
        this.date = date;
    }

    public Event toEntity(final Meeting meeting) {
        return new Event(date, meetingStartTime, meetingEndTime, meeting);
    }
}
