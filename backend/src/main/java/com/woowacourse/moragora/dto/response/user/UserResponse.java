package com.woowacourse.moragora.dto.response.user;

import com.woowacourse.moragora.domain.user.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String authProvider;

    public UserResponse(final Long id,
                        final String email,
                        final String nickname,
                        final String authProvider) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.authProvider = authProvider.toLowerCase();
    }

    public static UserResponse from(final User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProvider().name()
        );
    }
}
