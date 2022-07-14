package com.woowacourse.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.auth.dto.LoginResponse;
import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @DisplayName("로그인 정보를 받아 토큰을 생성한다.")
    @Test
    void createToken() {
        // given
        final String email = "kun@email.com";
        final String password = "qwerasdf123!";
        final UserRequest userRequest = new UserRequest(email, password, "kun");
        userService.create(userRequest);

        final LoginRequest loginRequest = new LoginRequest(email, password);

        // when
        final LoginResponse response = authService.createToken(loginRequest);

        // then
        assertThat(response.getAccessToken()).isNotNull();
    }
}
