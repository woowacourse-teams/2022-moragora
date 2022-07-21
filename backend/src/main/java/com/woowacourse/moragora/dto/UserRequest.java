package com.woowacourse.moragora.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRequest {

    @Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "입력 형식이 올바르지 않습니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$", message = "입력 형식이 올바르지 않습니다.")
    private String password;

    @Pattern(regexp = "[a-zA-Z0-9가-힣]{1,15}", message = "입력 형식이 올바르지 않습니다.")
    private String nickname;

    public UserRequest(final String email, final String password, final String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
