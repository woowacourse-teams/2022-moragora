package com.woowacourse.moragora.dto.request.user;

import com.woowacourse.moragora.constant.Patterns;
import com.woowacourse.moragora.constant.ValidationMessages;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.domain.user.password.EncodedPassword;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString(exclude = "password")
public class UserRequest {

    @Email(regexp = Patterns.EMAIL, message = ValidationMessages.INVALID_FORMAT)
    private String email;

    @Pattern(regexp = Patterns.PASSWORD, message = ValidationMessages.INVALID_FORMAT)
    private String password;

    @Pattern(regexp = Patterns.NICKNAME, message = ValidationMessages.INVALID_FORMAT)
    private String nickname;

    public UserRequest(final String email, final String password, final String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User toEntity() {
        return new User(email, EncodedPassword.fromRawValue(password), nickname);
    }
}
