package com.woowacourse.moragora.dto.request.user;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString(exclude = {"oldPassword", "newPassword"})
public class PasswordRequest {

    private String oldPassword;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$",
            message = "입력 형식이 올바르지 않습니다.")
    private String newPassword;

    public PasswordRequest(final String oldPassword, final String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
