package com.woowacourse.auth.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.moragora.acceptance.AcceptanceTest;
import com.woowacourse.moragora.dto.UserRequest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("인증 관련 기능")
class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인에 성공할 때 토큰과 상태코드 200을 반환한다.")
    @Test
    void login() {
        // given
        final String email = "kun@naver.com";
        final String password = "1234smart!";
        final UserRequest userRequest = new UserRequest(email, password, "kun");
        post("/users", userRequest);

        final LoginRequest loginRequest = new LoginRequest(email, password);

        // when
        final ValidatableResponse response = post("/login", loginRequest);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("accessToken", notNullValue());
    }

    @DisplayName("이메일 또는 비밀번호가 잘못되었을 때 로그인에 실패하고 상태코드 400을 반환한다.")
    @Test
    void login_fail() {
        // given
        final String email = "kun@naver.com";
        final String password = "1234smart!";
        final UserRequest userRequest = new UserRequest(email, password, "kun");
        post("/users", userRequest);

        final LoginRequest loginRequest = new LoginRequest(email, "password123!");

        // when
        final ValidatableResponse response = post("/login", loginRequest);

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("이메일이나 비밀번호가 틀렸습니다."));
    }
}
