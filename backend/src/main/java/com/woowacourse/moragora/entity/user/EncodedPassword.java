package com.woowacourse.moragora.entity.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class EncodedPassword {

    @Column(name = "password")
    private String value;

    public EncodedPassword(final RawPassword rawPassword) {
        this.value = rawPassword.encode();
    }
}
