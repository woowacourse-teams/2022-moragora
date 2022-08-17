package com.woowacourse.moragora.dto;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class NicknameRequest {

    @Pattern(regexp = "[a-zA-Z0-9가-힣]{1,15}", message = "입력 형식이 올바르지 않습니다.")
    private String nickname;

    public NicknameRequest(final String nickname) {
        this.nickname = nickname;
    }
}
