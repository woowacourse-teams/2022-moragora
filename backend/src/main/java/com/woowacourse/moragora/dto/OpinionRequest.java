package com.woowacourse.moragora.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OpinionRequest {

    @NotBlank(message = "내용을 작성하지 않았습니다.")
    private String content;

    public OpinionRequest(final String content) {
        this.content = content;
    }
}
