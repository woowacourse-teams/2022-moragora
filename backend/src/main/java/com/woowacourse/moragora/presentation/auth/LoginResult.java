package com.woowacourse.moragora.presentation.auth;

import lombok.Getter;

@Getter
public class LoginResult {

    private final String accessToken;
    private final String refreshToken;

    public LoginResult(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
