package com.woowacourse.moragora.entity.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.exception.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @DisplayName("입력받은 비밀번호로 암호화된 Password를 생성한다.")
    @Test
    void fromRawValue() {
        // given
        final String rawPassword = "1234smart!";

        // when
        final Password password = Password.fromRawValue(rawPassword);

        // then
        assertThat(password.getValue()).isNotEqualTo(rawPassword);
    }

    @DisplayName("형식에 맞지 않는 비밀번호로 Password를 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"password", "password!", "password1", "12345678!", "password!!123456789012345678901"})
    void fromRawValue_throwsException_invalidFormat(final String rawPassword) {
        // given, when, then
        assertThatThrownBy(() -> Password.fromRawValue(rawPassword))
                .isInstanceOf(InvalidFormatException.class);
    }
}
