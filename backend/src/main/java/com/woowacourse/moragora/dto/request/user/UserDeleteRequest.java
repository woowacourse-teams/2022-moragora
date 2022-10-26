package com.woowacourse.moragora.dto.request.user;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString(exclude = "password")
public class UserDeleteRequest {
    private static final String MISSING_REQUIRED_INPUT = "필수 입력 값이 누락됐습니다.";

    @NotBlank(message = MISSING_REQUIRED_INPUT)
    private String password;

    public UserDeleteRequest(final String password) {
        this.password = password;
    }
}
