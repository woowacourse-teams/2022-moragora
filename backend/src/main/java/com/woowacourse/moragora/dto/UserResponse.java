package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.user.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserResponse {

    private final Long id;
    private final String email;
    private final String nickname;

    public UserResponse(final Long id,
                        final String email,
                        final String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static UserResponse from(final User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }
}
