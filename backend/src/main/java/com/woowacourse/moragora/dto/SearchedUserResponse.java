package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.user.User;
import lombok.Getter;

@Getter
public class SearchedUserResponse {

    private final Long id;
    private final String email;
    private final String nickname;

    public SearchedUserResponse(final Long id, final String email, final String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static SearchedUserResponse from(final User user) {
        return new SearchedUserResponse(user.getId(), user.getEmail(), user.getNickname());
    }
}
