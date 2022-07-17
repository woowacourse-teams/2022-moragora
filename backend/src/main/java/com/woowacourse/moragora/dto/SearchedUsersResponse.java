package com.woowacourse.moragora.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class SearchedUsersResponse {

    private final List<SearchedUserResponse> users;

    public SearchedUsersResponse(final List<SearchedUserResponse> users) {
        this.users = users;
    }
}
