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
}
