package com.woowacourse.moragora.presentation;

import lombok.Getter;

@Getter
public enum SessionAttribute {

    EMAIL_VERIFICATION("emailVerification"),
    VERIFIED_EMAIL("verifiedEmail"),
    ;

    private final String name;

    SessionAttribute(final String name) {
        this.name = name;
    }
}
