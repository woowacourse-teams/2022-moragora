package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.user.EncodedPassword;
import com.woowacourse.moragora.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원 정보를 저장한다.")
    @Test
    void save() {
        // given
        final EncodedPassword encodedPassword = EncodedPassword.fromRawValue("asdfqer1!");
        final User user = new User("kun@naver.com", encodedPassword, "kun");

        // when
        final User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
    }

    @DisplayName("id로 유저를 검색할 수 있다.")
    @Test
    void findById() {
        // given, when
        final EncodedPassword encodedPassword = EncodedPassword.fromRawValue("asdfqer1!");
        final User savedUser = userRepository.save(new User("kun@naver.com", encodedPassword, "kun"));

        // when
        final User user = userRepository.findById(savedUser.getId()).get();

        assertThat(user).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedUser);
    }

    @DisplayName("여러 id로 여러명의 유저를 검색할 수 있다.")
    @Test
    void findByIds() {
        // given, when
        final List<User> users = userRepository.findByIds(List.of(1L, 2L, 3L));

        // then
        assertThat(users).hasSize(3);
    }

    @DisplayName("이메일로 유저를 검색한다.")
    @Test
    void findByEmail() {
        // given
        final String email = "kun@email.com";
        userRepository.save(new User(email, EncodedPassword.fromRawValue("qweradsf123!"), "kun"));

        // when
        final Optional<User> user = userRepository.findByEmail(email);

        // then
        assertThat(user.isPresent()).isTrue();
    }

    @DisplayName("keyword로 유저를 검색할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"foo,7", "ggg777@foo.com,1"})
    void findByNicknameOrEmailContaining(final String keyword, final int expectedSize) {
        // given, when
        final List<User> users = userRepository.findByNicknameOrEmailContaining(keyword);

        // then
        assertThat(users).hasSize(expectedSize);
    }
}
