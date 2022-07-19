package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.user.User;
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

    public static UserResponse2 from(final User user) {
        return new UserResponse2(user.getId(), user.getEmail(), user.getNickname());
    }
}
