package com.woowacourse.moragora.dto;

import lombok.Getter;

@Getter
public class UserResponse2 {

    private final Long id;
    private final String email;
    private final String nickname;

    public UserResponse2(final Long id,
                         final String email,
                         final String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
