package com.woowacourse.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString(exclude = "password")
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
