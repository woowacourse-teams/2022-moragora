package com.woowacourse.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.auth.dto.LoginResponse;
import com.woowacourse.auth.exception.LoginFailException;
import com.woowacourse.moragora.controller.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

public class AuthControllerTest extends ControllerTest {

    @DisplayName("로그인에 성공한다.")
    @Test
    void login() throws Exception {
        // given
        final LoginRequest loginRequest = new LoginRequest("kun@naver.com", "1234smart!");
        final String accessToken = "fake_token";

        given(authService.createToken(any(LoginRequest.class)))
                .willReturn(new LoginResponse(accessToken));

        // when
        final ResultActions resultActions = performPost("/login", loginRequest);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").value(accessToken));
    }

    @DisplayName("로그인에 실패한다.")
    @Test
    void login_fail() throws Exception {
        // given
        final LoginRequest loginRequest = new LoginRequest("kun@naver.com", "1234smart!");

        given(authService.createToken(any(LoginRequest.class)))
                .willThrow(new LoginFailException());

        // when
        final ResultActions resultActions = performPost("/login", loginRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("이메일이나 비밀번호가 틀렸습니다."));
    }
}
