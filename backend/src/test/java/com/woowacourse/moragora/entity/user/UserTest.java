package com.woowacourse.moragora.entity.user;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.exception.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

    @DisplayName("회원을 생성한다.")
    @Test
    void createUser() {
        // given
        final EncodedPassword encodedPassword = new EncodedPassword(new RawPassword("asdfqer1!"));

        // when, then
        assertThatCode(() -> new User("kun@email.com", encodedPassword, "kun"))
                .doesNotThrowAnyException();
    }

    @DisplayName("유효하지 않은 이메일로 회원을 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"kun", "kun@", "@email.com", "kun@com"})
    void createUser_throwsException_IfInvalidEmail(final String email) {
        // given
        final EncodedPassword encodedPassword = new EncodedPassword(new RawPassword("asdfqer1!"));

        // when, then
        assertThatThrownBy(() -> new User(email, encodedPassword, "kun"))
                .isInstanceOf(InvalidFormatException.class);
    }

    @DisplayName("유효하지 않은 닉네임로 회원을 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"smart쿤!", "smartboykun12345", "smart kun"})
    void createUser_throwsException_IfInvalidNickname(final String nickname) {
        // given
        final EncodedPassword encodedPassword = new EncodedPassword(new RawPassword("asdfqer1!"));

        // when, then
        assertThatThrownBy(() -> new User("kun@email.com", encodedPassword, nickname))
                .isInstanceOf(InvalidFormatException.class);
    }
}
