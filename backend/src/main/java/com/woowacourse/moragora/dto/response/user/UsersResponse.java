package com.woowacourse.moragora.dto.response.user;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UsersResponse {

    private final List<UserResponse> users;

    public UsersResponse(final List<UserResponse> users) {
        this.users = users;
    }
}
