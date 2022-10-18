package com.woowacourse.moragora.dto.request.auth;

import com.woowacourse.moragora.constant.Patterns;
import com.woowacourse.moragora.constant.ValidationMessages;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
@ToString
public class EmailVerifyRequest {

    private static final int CODE_LENGTH = 6;

    @Email(regexp = Patterns.EMAIL, message = ValidationMessages.INVALID_FORMAT)
    private String email;

    @NotBlank(message = ValidationMessages.MISSING_REQUIRED_INPUT)
    @Length(min = CODE_LENGTH, max = CODE_LENGTH, message = "인증 번호는 " + CODE_LENGTH + "자 입니다.")
    private String verifyCode;

    public EmailVerifyRequest(final String email, final String verifyCode) {
        this.email = email;
        this.verifyCode = verifyCode;
    }
}
