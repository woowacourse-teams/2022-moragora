package com.woowacourse.moragora.support;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.support.CryptoEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CryptoEncoderTest {

    @DisplayName("같은 문자열을 암호화하면 같은 값이 나온다.")
    @Test
    void encrypt_ifArgumentIsSame() {
        // given
        final String rawPassword = "1234smart!";

        // when
        final String encodedPassword1 = CryptoEncoder.encrypt(rawPassword);
        final String encodedPassword2 = CryptoEncoder.encrypt(rawPassword);

        // then
        assertThat(encodedPassword1).isEqualTo(encodedPassword2);
    }
}
