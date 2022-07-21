package com.woowacourse.moragora.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class UsersResponse {

    private final List<UserResponse> users;

    public UsersResponse(final List<UserResponse> users) {
        this.users = users;
    }
}
