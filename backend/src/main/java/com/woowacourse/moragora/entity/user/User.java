package com.woowacourse.moragora.entity.user;

import com.woowacourse.moragora.exception.InvalidFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
public class User {

    private static final String REGEX_EMAIL = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    private static final Pattern PATTERN_EMAIL = Pattern.compile(REGEX_EMAIL);

    private static final String REGEX_NICKNAME = "[a-zA-Z0-9가-힣]{1,15}";
    private static final Pattern PATTERN_NICKNAME = Pattern.compile(REGEX_NICKNAME);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Password password;

    @Column(nullable = false)
    private String nickname;

    public User(final String email, final String password, final String nickname) {
        validateEmail(email);
        validateNickname(nickname);
        this.email = email;
        this.password = Password.fromRawValue(password);
        this.nickname = nickname;
    }

    private void validateEmail(final String email) {
        final Matcher matcher = PATTERN_EMAIL.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidFormatException();
        }
    }

    private void validateNickname(final String nickname) {
        final Matcher matcher = PATTERN_NICKNAME.matcher(nickname);
        if (!matcher.matches()) {
            throw new InvalidFormatException();
        }
    }
}
