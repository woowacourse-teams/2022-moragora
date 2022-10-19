package com.woowacourse.moragora.dto.request.event;

import com.woowacourse.moragora.constant.Patterns;
import com.woowacourse.moragora.constant.ValidationMessages;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
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

    @NotNull(message = ValidationMessages.MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = Patterns.TIME)
    private LocalTime meetingStartTime;

    @NotNull(message = ValidationMessages.MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = Patterns.TIME)
    private LocalTime meetingEndTime;

    @NotNull(message = ValidationMessages.MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = Patterns.DATE)
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
