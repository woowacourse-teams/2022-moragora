package com.woowacourse.moragora.infrastructure;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

class GoogleMailSenderTest {

    private final JavaMailSender javaMailSender = mock(JavaMailSender.class);
    private final GoogleMailSender googleMailSender = new GoogleMailSender(javaMailSender);

    @DisplayName("google 메일을 전송한다.")
    @Test
    void send() {
        // when
        googleMailSender.send("email", "authcode");

        // then
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
