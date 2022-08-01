package com.woowacourse.moragora.support;

import com.woowacourse.moragora.entity.user.EncodedPassword;
import com.woowacourse.moragora.entity.user.User;
import java.util.List;

public enum UserFixtures {

    KUN("kun@email.com", EncodedPassword.fromRawValue("1234asdf!"), "kun"),
    AZPI("aspi@email.com", EncodedPassword.fromRawValue("1234asdf!"), "aspi"),
    SUN("sun@email.com", EncodedPassword.fromRawValue("1234asdf!"), "sun"),
    FORKY("forky@email.com", EncodedPassword.fromRawValue("1234asdf!"), "forky"),
    PHILLZ("phillz@email.com", EncodedPassword.fromRawValue("1234asdf!"), "phillz"),
    WOODY("woodi@email.com", EncodedPassword.fromRawValue("1234asdf!"), "woody"),
    BATD("batd@email.com", EncodedPassword.fromRawValue("1234asdf!"), "batd");

    private final String email;

    private final EncodedPassword password;

    private final String nickname;

    UserFixtures(final String email, final EncodedPassword password, final String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public static List<User> createUsers() {
        return List.of(KUN.create(), AZPI.create(), SUN.create(), FORKY.create(), PHILLZ.create(), WOODY.create(),
                BATD.create());
    }

    public User create() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }
}
