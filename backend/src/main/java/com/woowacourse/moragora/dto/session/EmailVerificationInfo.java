package com.woowacourse.moragora.dto.session;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailVerificationInfo {

    private String email;

    private String code;

    private long expiredTime;

    public EmailVerificationInfo(final String email, final String code, final LocalDateTime expiredTime) {
        this.email = email;
        this.code = code;
        this.expiredTime = Timestamp.valueOf(expiredTime).getTime();
    }
}
