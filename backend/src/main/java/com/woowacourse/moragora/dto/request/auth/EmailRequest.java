package com.woowacourse.moragora.dto.request.auth;

import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class EmailRequest {

    @Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "입력 형식이 올바르지 않습니다.")
    private String email;

    public EmailRequest(final String email) {
        this.email = email;
    }
}
