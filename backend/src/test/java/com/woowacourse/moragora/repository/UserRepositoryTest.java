package com.woowacourse.moragora.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.entity.user.EncodedPassword;
import com.woowacourse.moragora.entity.user.RawPassword;
import com.woowacourse.moragora.entity.user.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        final EncodedPassword encodedPassword = new EncodedPassword(new RawPassword("asdfqer1!"));
        final User user = new User("kun@naver.com", encodedPassword, "kun");

        // when
        final User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
    }

    @DisplayName("여러 id로 여러명의 유저를 검색할 수 있다.")
    @Test
    void findByIds() {
        final List<User> users = userRepository.findByIds(List.of(1L, 2L, 3L));

        assertThat(users).hasSize(3);
    }
}
