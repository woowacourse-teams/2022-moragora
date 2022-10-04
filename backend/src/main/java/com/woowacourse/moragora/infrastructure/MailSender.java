package com.woowacourse.moragora.infrastructure;

public interface MailSender {

    void send(final String email, final String authCode);
}
