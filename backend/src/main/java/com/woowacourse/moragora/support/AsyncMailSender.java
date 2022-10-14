package com.woowacourse.moragora.support;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncMailSender {

    private final JavaMailSender javaMailSender;

    public AsyncMailSender(final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void send(final SimpleMailMessage simpleMailMessage) {
        javaMailSender.send(simpleMailMessage);
    }
}
