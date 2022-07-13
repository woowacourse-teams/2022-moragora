package com.woowacourse.moragora.entity.user;

import com.woowacourse.moragora.exception.InvalidFormatException;
import com.woowacourse.moragora.util.CryptoEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Password {

    private static final String REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "password")
    private String value;

    private Password(final String value) {
        this.value = value;
    }

    public static Password fromRawValue(final String rawPassword) {
        validateFormat(rawPassword);
        final String encodedPassword = CryptoEncoder.encrypt(rawPassword);
        return new Password(encodedPassword);
    }

    private static void validateFormat(final String rawPassword) {
        final Matcher matcher = PATTERN.matcher(rawPassword);
        if (!matcher.matches()) {
            throw new InvalidFormatException();
        }
    }
}
