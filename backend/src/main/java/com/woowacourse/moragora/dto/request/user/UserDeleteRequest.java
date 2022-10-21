package com.woowacourse.moragora.dto.request.user;

import com.woowacourse.moragora.constant.ValidationMessages;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString(exclude = "password")
public class UserDeleteRequest {

    @NotBlank(message = ValidationMessages.MISSING_REQUIRED_INPUT)
    private String password;

    public UserDeleteRequest(final String password) {
        this.password = password;
    }
}
