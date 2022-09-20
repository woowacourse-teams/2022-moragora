package com.woowacourse.moragora.domain.user.password;

import com.woowacourse.moragora.exception.global.InvalidFormatException;
import com.woowacourse.moragora.support.CryptoEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;

@Getter
public class RawPassword {

    private static final String REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private final String value;

    public RawPassword(final String value) {
        validateFormat(value);
        this.value = value;
    }

    private void validateFormat(final String value) {
        final Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new InvalidFormatException();
        }
    }

    public String encode() {
        return CryptoEncoder.encrypt(value);
    }
}
