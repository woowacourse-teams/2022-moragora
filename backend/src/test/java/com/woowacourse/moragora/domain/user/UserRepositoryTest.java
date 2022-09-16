package com.woowacourse.moragora.domain.user;

import static com.woowacourse.moragora.support.fixture.UserFixtures.FORKY;
import static com.woowacourse.moragora.support.fixture.UserFixtures.KUN;
import static com.woowacourse.moragora.support.fixture.UserFixtures.SUN;
import static com.woowacourse.moragora.support.fixture.UserFixtures.createUsers;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원 정보를 저장한다.")
    @Test
    void save() {
        // given
        final User user = KUN.create();

        // when
        final User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
    }

    @DisplayName("id로 유저를 검색할 수 있다.")
    @Test
    void findById() {
        // given
        final User user = KUN.create();
        final User savedUser = userRepository.save(user);

        // when
        final User foundUser = userRepository.findById(savedUser.getId()).get();

        assertThat(foundUser).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedUser);
    }

    @DisplayName("여러 id로 여러명의 유저를 검색할 수 있다.")
    @Test
    void findByIdIn() {
        // given
        final User user1 = userRepository.save(KUN.create());
        final User user2 = userRepository.save(SUN.create());
        final User user3 = userRepository.save(FORKY.create());

        // when
        final List<User> users = userRepository.findByIdIn(List.of(user1.getId(), user2.getId(), user3.getId()));

        // then
        assertThat(users.containsAll(List.of(user1, user2, user3))).isTrue();
    }

    @DisplayName("이메일로 유저를 검색한다.")
    @Test
    void findByEmail() {
        // given
        final User user = KUN.create();
        final User savedUser = userRepository.save(user);

        // when
        final Optional<User> foundUser = userRepository.findByEmail(savedUser.getEmail());

        // then
        assertThat(foundUser.isPresent()).isTrue();
    }

    @DisplayName("keyword로 유저를 검색할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"kun,1", "email,7"})
    void findByNicknameOrEmailContaining(final String keyword, final int expectedSize) {
        // given
        final List<User> users = createUsers();
        for (User user : users) {
            userRepository.save(user);
        }

        // when
        final List<User> foundUsers = userRepository.findByNicknameOrEmailLike(keyword);

        // then
        assertThat(foundUsers).hasSize(expectedSize);
    }
}
