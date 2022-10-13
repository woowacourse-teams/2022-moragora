package com.woowacourse.moragora.domain.auth;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    public AuthCode(final String email, final LocalDateTime dateTime) {
        this.email = email;
        this.code = generateAuthCode();
        this.expiredTime = Timestamp.valueOf(dateTime.plusMinutes(AUTH_CODE_EXPIRE_MINUTE)).getTime();
    }

    private String generateAuthCode() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < AUTH_CODE_LENGTH; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    public SimpleMailMessage toMailMessage() {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(AUTH_CODE_SUBJECT);
        message.setText(AUTH_CODE_PREFIX + code);
        return message;
    }
}
