package com.woowacourse.moragora.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OpinionRequest {

    private String content;

    public OpinionRequest(final String content) {
        this.content = content;
    }
}
