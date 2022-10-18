package com.woowacourse.moragora.domain.auth;

import static com.woowacourse.moragora.domain.auth.AuthCode.AUTH_CODE_EXPIRE_MINUTE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.moragora.exception.auth.AuthCodeException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;

class AuthCodeTest {

    @DisplayName("인증번호 메일을 작성한다.")
    @Test
    void toMailMessage() {
        final String email = "forky@email.com";
        final String code = "000000";
        final LocalDateTime dateTime = LocalDateTime.now();
        final AuthCode authCode = new AuthCode(email, code, dateTime);

        final SimpleMailMessage simpleMailMessage = authCode.toMailMessage();
        assertAll(
                () -> assertThat(simpleMailMessage.getTo()[0]).isEqualTo(email),
                () -> assertThat(simpleMailMessage.getText()).endsWith(code)
        );
    }

    @DisplayName("인증번호를 검증한다.")
    @Test
    void verify() {
        final String email = "forky@email.com";
        final String code = "000000";
        final LocalDateTime dateTime = LocalDateTime.now();
        final AuthCode authCode = new AuthCode(email, code, dateTime);

        assertThatCode(() -> authCode.verify(email, code, dateTime))
                .doesNotThrowAnyException();
    }

    @DisplayName("잘못된 메일로 인증 번호를 검증하면 예외가 발생한다.")
    @Test
    void verify_throwsException_ifWrongEmail() {
        final String email = "forky@email.com";
        final String code = "000000";
        final LocalDateTime dateTime = LocalDateTime.now();
        final AuthCode authCode = new AuthCode(email, code, dateTime);

        assertThatThrownBy(() -> authCode.verify("wrong@email.com", code, dateTime))
                .isInstanceOf(AuthCodeException.class)
                .hasMessage("인증을 요청하지 않은 이메일입니다.");
    }

    @DisplayName("잘못된 인증 번호를 검증하면 예외가 발생한다.")
    @Test
    void verify_throwsException_ifWrongCode() {
        final String email = "forky@email.com";
        final String code = "000000";
        final LocalDateTime dateTime = LocalDateTime.now();
        final AuthCode authCode = new AuthCode(email, code, dateTime);

        assertThatThrownBy(() -> authCode.verify(email, "wrong", dateTime))
                .isInstanceOf(AuthCodeException.class)
                .hasMessage("인증코드가 올바르지 않습니다.");
    }

    @DisplayName("만료된 인증 번호를 검증하면 예외가 발생한다.")
    @Test
    void verify_throwsException_ifExpiredCode() {
        final String email = "forky@email.com";
        final String code = "000000";
        final LocalDateTime dateTime = LocalDateTime.now();
        final AuthCode authCode = new AuthCode(email, code, dateTime);

        assertThatThrownBy(
                () -> authCode.verify(email, code, dateTime.plusMinutes(AUTH_CODE_EXPIRE_MINUTE + 1)))
                .isInstanceOf(AuthCodeException.class)
                .hasMessage("인증코드가 만료되었습니다.");
    }
}
