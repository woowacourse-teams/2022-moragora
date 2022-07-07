package com.woowacourse.moragora.dto;

import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String name;
    private final int absentCount;

    public UserResponse(final Long id, final String name, final int absentCount) {
        this.id = id;
        this.name = name;
        this.absentCount = absentCount;
    }
}
