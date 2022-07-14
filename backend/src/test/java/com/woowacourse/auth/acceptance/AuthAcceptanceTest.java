package com.woowacourse.auth.acceptance;

import static org.hamcrest.Matchers.notNullValue;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.moragora.acceptance.AcceptanceTest;
import com.woowacourse.moragora.dto.UserRequest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인에 성공할 때 토큰을 발급한다.")
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
}
