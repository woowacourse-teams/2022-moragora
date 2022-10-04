package com.woowacourse.moragora.infrastructure;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class GoogleMailSender implements MailSender {

    private static final String AUTH_CODE_SUBJECT = "체크메이트 인증코드입니다.";
    private static final String AUTH_CODE_PREFIX = "인증 코드 : ";

    private final JavaMailSender javaMailSender;

    public GoogleMailSender(final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(final String email, final String authCode) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(AUTH_CODE_SUBJECT);
        message.setText(AUTH_CODE_PREFIX + authCode);

        javaMailSender.send(message);
    }
}
