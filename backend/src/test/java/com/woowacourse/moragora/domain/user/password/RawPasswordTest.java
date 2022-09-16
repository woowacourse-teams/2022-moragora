package com.woowacourse.moragora.domain.user.password;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.exception.global.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RawPasswordTest {

    @DisplayName("형식에 맞지 않는 비밀번호로 Password를 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"password", "password!", "password1", "12345678!", "password!!123456789012345678901"})
    void create_throwsException_invalidFormat(final String value) {
        // given, when, then
        assertThatThrownBy(() -> new RawPassword(value))
                .isInstanceOf(InvalidFormatException.class);
    }

    @DisplayName("입력받은 비밀번호로 암호화된 Password를 생성한다.")
    @Test
    void encode() {
        // given
        final String value = "1234Pass!";
        final RawPassword rawPassword = new RawPassword(value);

        // when
        final String encodedPassword = rawPassword.encode();

        // then
        assertThat(value).isNotEqualTo(encodedPassword);
    }
}
