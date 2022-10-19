package com.woowacourse.moragora.dto.request.user;

import com.woowacourse.moragora.constant.Patterns;
import com.woowacourse.moragora.constant.ValidationMessages;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class NicknameRequest {

    @Pattern(regexp = Patterns.NICKNAME, message = ValidationMessages.INVALID_FORMAT)
    private String nickname;

    public NicknameRequest(final String nickname) {
        this.nickname = nickname;
    }
}
