package com.woowacourse.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.auth.dto.LoginResponse;
import com.woowacourse.auth.exception.AuthorizationFailureException;
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

    @DisplayName("존재하지 않는 이메일로 로그인 시도시 예외가 발생한다.")
    @Test
    void createToken_throwsException_ifEmailIsNotExist() {
        // given
        final String email = "kun@email.com";
        final String password = "qwerasdf123!";

        final LoginRequest loginRequest = new LoginRequest(email, password);

        // when, then
        assertThatThrownBy(() -> authService.createToken(loginRequest))
                .isInstanceOf(AuthorizationFailureException.class);
    }

    @DisplayName("잘못된 비밀번호로 로그인 시도시 예외가 발생한다.")
    @Test
    void createToken_throwsException_ifPasswordIsWrong() {
        // given
        final String email = "kun@email.com";
        final String password = "qwerasdf123!";
        final UserRequest userRequest = new UserRequest(email, password, "kun");
        userService.create(userRequest);

        final LoginRequest loginRequest = new LoginRequest(email, "smart1234!");

        // when, then
        assertThatThrownBy(() -> authService.createToken(loginRequest))
                .isInstanceOf(AuthorizationFailureException.class);
    }

    @DisplayName("해당 유저가 해당 미팅의 마스터인지 체크한다.")
    @Test
    void isMaster() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 1L;

        // when, then
        assertThat(authService.isMaster(meetingId, loginId)).isTrue();
    }

    @DisplayName("해당 유저가 해당 미팅의 마스터인지 체크한다(아닌 경우)")
    @Test
    void isMaster_Not() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 2L;

        // when, then
        assertThat(authService.isMaster(meetingId, loginId)).isFalse();
    }
}
