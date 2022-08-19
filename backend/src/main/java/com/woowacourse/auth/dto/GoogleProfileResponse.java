package com.woowacourse.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class GoogleProfileResponse {

    private String email;
    private String name;

    public GoogleProfileResponse(final String email, final String name) {
        this.email = email;
        this.name = name;
    }
}
