package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRequest {

    private String email;
    private String password;
    private String nickname;

    public UserRequest(final String email, final String password, final String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User toEntity() {
        return new User(email, password, nickname);
    }
}
