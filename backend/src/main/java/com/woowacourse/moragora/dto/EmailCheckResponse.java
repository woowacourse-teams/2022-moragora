package com.woowacourse.moragora.dto;

import lombok.Getter;

@Getter
public class EmailCheckResponse {

    private final Boolean isExist; // primitive로 두면 jackson에서 field명이 exist로 바뀜

    public EmailCheckResponse(final boolean isExist) {
        this.isExist = isExist;
    }
}
