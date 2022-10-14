package com.woowacourse.moragora.dto.request.auth;

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
    private static final String MISSING_REQUIRED_INPUT = "필수 입력 값이 누락됐습니다.";

    @Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "입력 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = MISSING_REQUIRED_INPUT)
    @Length(min = CODE_LENGTH, max = CODE_LENGTH, message = "인증 번호는 " + CODE_LENGTH + "자 입니다.")
    private String verifyCode;

    public EmailVerifyRequest(final String email, final String verifyCode) {
        this.email = email;
        this.verifyCode = verifyCode;
    }
}
