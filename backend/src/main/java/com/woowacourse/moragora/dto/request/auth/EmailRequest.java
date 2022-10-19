package com.woowacourse.moragora.dto.request.auth;

import com.woowacourse.moragora.constant.Patterns;
import com.woowacourse.moragora.constant.ValidationMessages;
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class EmailRequest {

    @Email(regexp = Patterns.EMAIL, message = ValidationMessages.INVALID_FORMAT)
    private String email;

    public EmailRequest(final String email) {
        this.email = email;
    }
}
