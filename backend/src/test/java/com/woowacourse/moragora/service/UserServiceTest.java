package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.dto.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @DisplayName("새로운 회원을 생성한다.")
    @Test
    void create() {
        // given
        final UserRequest userRequest = new UserRequest("kun@naver.com", "1234smart!", "kun");

        // when
        final Long id = userService.create(userRequest);

        // then
        assertThat(id).isNotNull();
    }

    @DisplayName("중복되지 않은 이메일의 중복 여부를 확인한다.")
    @Test
    void isEmailExist_ifNotExists() {
        // given
        final String uniqueEmail = "someUniqueEmail@email.unique";

        // when
        final boolean isExist = userService.isEmailExist(uniqueEmail);

        // then
        assertThat(isExist).isFalse();
    }

    @DisplayName("중복된 이메일의 중복 여부를 확인한다.")
    @Test
    void isEmailExist_ifExists() {
        // given
        final String existingEmail = "kun@naver.com";
        final UserRequest userRequest = new UserRequest(existingEmail, "1234smart!", "kun");
        userService.create(userRequest);

        // when
        final boolean isExist = userService.isEmailExist(existingEmail);

        // then
        assertThat(isExist).isTrue();
    }
}
