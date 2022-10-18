package com.woowacourse.moragora.dto.request.event;

import com.woowacourse.moragora.constant.Patterns;
import com.woowacourse.moragora.constant.ValidationMessages;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@Getter
@ToString
public class EventCancelRequest {

    @NotNull(message = ValidationMessages.MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = Patterns.DATE)
    private List<LocalDate> dates;

    public EventCancelRequest(final List<LocalDate> dates) {
        this.dates = dates;
    }
}
