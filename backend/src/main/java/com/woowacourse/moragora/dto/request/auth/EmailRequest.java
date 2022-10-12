package com.woowacourse.moragora.dto.request.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailRequest {

    private String email;

    public EmailRequest(final String email) {
        this.email = email;
    }
}
