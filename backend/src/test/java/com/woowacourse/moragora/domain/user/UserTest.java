package com.woowacourse.moragora.domain.user;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.domain.user.password.EncodedPassword;
import com.woowacourse.moragora.domain.user.password.RawPassword;
import com.woowacourse.moragora.exception.global.InvalidFormatException;
import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

    @DisplayName("회원을 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "kun",
            "sunnnnnnny",
            "imsmartboyimsmartboyimsmartboyimsmartboyimsmartboy"
    })
    void createUser(final String nickname) {
        // given
        final EncodedPassword encodedPassword = EncodedPassword.fromRawValue("asdfqer1!");

        // when, then
        assertThatCode(() -> new User("kun@email.com", encodedPassword, nickname))
                .doesNotThrowAnyException();
    }

    @DisplayName("유효하지 않은 이메일로 회원을 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"kun", "kun@", "@email.com", "kun@com"})
    void createUser_throwsException_IfInvalidEmail(final String email) {
        // given
        final EncodedPassword encodedPassword = EncodedPassword.fromRawValue("asdfqer1!");

        // when, then
        assertThatThrownBy(() -> new User(email, encodedPassword, "kun"))
                .isInstanceOf(InvalidFormatException.class);
    }

    @DisplayName("유효하지 않은 닉네임로 회원을 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "imsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboy1",
            "imsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboy123"
    })
    void createUser_throwsException_IfInvalidNickname(final String nickname) {
        // given
        final EncodedPassword encodedPassword = EncodedPassword.fromRawValue("asdfqer1!");

        // when, then
        assertThatThrownBy(() -> new User("kun@email.com", encodedPassword, nickname))
                .isInstanceOf(InvalidFormatException.class);
    }

    @DisplayName("입력한 비밀번호와 자신의 비밀번호를 비교한다.")
    @Test
    void checkPassword() {
        // given
        final String plainPassword = "rightPassword123!";
        final User user = new User("asdf@naver.com", EncodedPassword.fromRawValue(plainPassword), "kun");

        // when, then
        assertThatCode(() -> user.checkPassword(new RawPassword(plainPassword)))
                .doesNotThrowAnyException();
    }

    @DisplayName("입력한 비밀번호와 자신의 비밀번호와 다를시 예외가 발생한다..")
    @Test
    void checkPassword_throwsException_ifPasswordIsDifferent() {
        // given
        final String plainPassword = "rightPassword123!";
        final User user = new User("asdf@naver.com", EncodedPassword.fromRawValue(plainPassword), "kun");
        final String wrongPassword = "wrongPassword123!";

        // when, then
        assertThatThrownBy(() -> user.checkPassword(new RawPassword(wrongPassword)))
                .isInstanceOf(AuthenticationFailureException.class);
    }

    @DisplayName("유효하지 않은 닉네임으로 유저의 닉네임을 변경하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "imsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboy1",
            "imsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboyimsmartboy123"
    })
    void updateNickname_throwsException_ifInvalidNickname(final String nickname) {
        // given
        final User user = new User("kun@email.com", EncodedPassword.fromRawValue("asdfqer1!"), "kun");

        // when, then
        assertThatThrownBy(() -> user.updateNickname(nickname))
                .isInstanceOf(InvalidFormatException.class);
    }
}
