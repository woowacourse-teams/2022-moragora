package com.woowacourse.moragora.dto.request.user;

import com.woowacourse.moragora.constant.ValidationMessages;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class UserAttendanceRequest {

    @NotNull(message = ValidationMessages.MISSING_REQUIRED_INPUT)
    private Boolean isPresent;

    public UserAttendanceRequest(final Boolean isPresent) {
        this.isPresent = isPresent;
    }
}
