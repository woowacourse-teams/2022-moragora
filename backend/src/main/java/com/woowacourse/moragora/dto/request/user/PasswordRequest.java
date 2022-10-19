package com.woowacourse.moragora.dto.request.user;

import com.woowacourse.moragora.constant.Patterns;
import com.woowacourse.moragora.constant.ValidationMessages;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString(exclude = {"oldPassword", "newPassword"})
public class PasswordRequest {

    private String oldPassword;

    @Pattern(regexp = Patterns.PASSWORD, message = ValidationMessages.INVALID_FORMAT)
    private String newPassword;

    public PasswordRequest(final String oldPassword, final String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
