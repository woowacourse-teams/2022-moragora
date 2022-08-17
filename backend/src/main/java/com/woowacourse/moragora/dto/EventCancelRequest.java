package com.woowacourse.moragora.dto;

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

    private static final String MISSING_REQUIRED_INPUT = "필수 입력 값이 누락됐습니다.";

    @NotNull(message = MISSING_REQUIRED_INPUT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> dates;

    public EventCancelRequest(final List<LocalDate> dates) {
        this.dates = dates;
    }
}
