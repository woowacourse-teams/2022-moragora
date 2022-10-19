package com.woowacourse.moragora.dto.request.meeting;

import com.woowacourse.moragora.constant.ValidationMessages;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class MasterRequest {

    @NotNull(message = ValidationMessages.MISSING_REQUIRED_INPUT)
    private Long userId;

    public MasterRequest(final Long userId) {
        this.userId = userId;
    }
}
