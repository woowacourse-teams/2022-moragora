package com.woowacourse.moragora.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class UsersResponse {

    private final List<UserResponse2> users;

    public UsersResponse(final List<UserResponse2> users) {
        this.users = users;
    }
}
