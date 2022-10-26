package com.woowacourse.moragora.dto.request.meeting;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class MasterRequest {
    private static final String MISSING_REQUIRED_INPUT = "필수 입력 값이 누락됐습니다.";

    @NotNull(message = MISSING_REQUIRED_INPUT)
    private Long userId;

    public MasterRequest(final Long userId) {
        this.userId = userId;
    }
}
