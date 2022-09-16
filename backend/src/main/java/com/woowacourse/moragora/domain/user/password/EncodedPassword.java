package com.woowacourse.moragora.domain.user.password;

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

    private EncodedPassword(final String encodedValue) {
        this.value = encodedValue;
    }

    public static EncodedPassword fromRawValue(final String plainValue) {
        final RawPassword rawPassword = new RawPassword(plainValue);
        return new EncodedPassword(rawPassword.encode());
    }

    public boolean isSamePassword(final RawPassword rawPassword) {
        return value.equals(rawPassword.encode());
    }
}
