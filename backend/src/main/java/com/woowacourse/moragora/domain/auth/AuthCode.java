package com.woowacourse.moragora.domain.auth;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;

@NoArgsConstructor
@Getter
public class AuthCode {

    private static final int AUTH_CODE_LENGTH = 6;
    private static final int AUTH_CODE_EXPIRE_MINUTE = 5;
    private static final String AUTH_CODE_SUBJECT = "체크메이트 인증코드입니다.";
    private static final String AUTH_CODE_PREFIX = "인증 코드 : ";

    private String email;
    private String code;
    private long expiredTime;

    public AuthCode(final String email, final String code, final LocalDateTime dateTime) {
        this.email = email;
        this.code = code;
        this.expiredTime = toTimeStamp(dateTime.plusMinutes(AUTH_CODE_EXPIRE_MINUTE));
    }

    public SimpleMailMessage toMailMessage() {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(AUTH_CODE_SUBJECT);
        message.setText(AUTH_CODE_PREFIX + code);
        return message;
    }

    public void verify(final String email, final String code, final LocalDateTime dateTime) {
        if (!this.email.equals(email)) {
            throw new ClientRuntimeException("인증을 요청하지 않은 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
        if (!this.code.equals(code)) {
            throw new ClientRuntimeException("인증코드가 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        if (toTimeStamp(dateTime) > this.expiredTime) {
            throw new ClientRuntimeException("인증코드가 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private long toTimeStamp(final LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime).getTime();
    }
}
