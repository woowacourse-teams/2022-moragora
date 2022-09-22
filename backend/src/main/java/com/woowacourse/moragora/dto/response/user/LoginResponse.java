package com.woowacourse.moragora.dto.response.user;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = "accessToken")
public class LoginResponse {

    private final String accessToken;

    public LoginResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
