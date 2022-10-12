package com.woowacourse.moragora.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.request.user.UserRequest;
import com.woowacourse.moragora.dto.response.user.GoogleProfileResponse;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
                .body("accessToken", notNullValue())
                .cookie("refreshToken", notNullValue());
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


    @DisplayName("구글 로그인에 성공할 때 토큰과 상태코드 200을 반환한다.")
    @Test
    void loginWithGoogle() {
        // given
        final String email = "kun@naver.com";
        final String password = "1234smart!";
        final UserRequest userRequest = new UserRequest(email, password, "kun");

        final LoginRequest loginRequest = new LoginRequest(email, password);

        given(googleClient.getIdToken(anyString()))
                .willReturn("fakeIdToken");
        given(googleClient.getProfileResponse(anyString()))
                .willReturn(new GoogleProfileResponse("sunny@gmail.com", "썬"));

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/oauth2/google?code=anyCode")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("accessToken", notNullValue());
    }


}
