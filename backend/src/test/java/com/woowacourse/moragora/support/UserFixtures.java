package com.woowacourse.moragora.support;

import com.woowacourse.moragora.entity.user.EncodedPassword;
import com.woowacourse.moragora.entity.user.User;
import java.util.List;

public enum UserFixtures {

    KUN("kun@email.com", EncodedPassword.fromRawValue("1234asdf!"), "kun"),
    AZPI("azpi@email.com", EncodedPassword.fromRawValue("1234asdf!"), "azpi"),
    SUN("sun@email.com", EncodedPassword.fromRawValue("1234asdf!"), "sun"),
    FORKY("forky@email.com", EncodedPassword.fromRawValue("1234asdf!"), "forky"),
    PHILLZ("phillz@email.com", EncodedPassword.fromRawValue("1234asdf!"), "phillz"),
    WOODY("woodi@email.com", EncodedPassword.fromRawValue("1234asdf!"), "woody"),
    BATD("batd@email.com", EncodedPassword.fromRawValue("1234asdf!"), "batd"),
    MASTER("master@email.com", EncodedPassword.fromRawValue("1234asdf!"), "master"),
    NO_MASTER("nomaster@email.com", EncodedPassword.fromRawValue("1234asdf!"), "noMaster");

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

    public static List<String> getEmailsIncludingMaster() {
        return List.of(
                KUN.email, AZPI.email, SUN.email, FORKY.email, PHILLZ.email, WOODY.email, BATD.email, MASTER.email
        );
    }

    public static List<String> getNicknamesIncludingMaster() {
        return List.of(
                KUN.nickname, AZPI.nickname, SUN.nickname, FORKY.nickname,
                PHILLZ.nickname, WOODY.nickname, BATD.nickname, MASTER.nickname
        );
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
