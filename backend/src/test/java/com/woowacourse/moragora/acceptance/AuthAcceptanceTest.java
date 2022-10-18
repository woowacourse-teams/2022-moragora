package com.woowacourse.moragora.acceptance;

import static com.woowacourse.moragora.support.fixture.UserFixtures.AZPI;
import static com.woowacourse.moragora.support.fixture.UserFixtures.SUN;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.request.user.UserRequest;
import com.woowacourse.moragora.dto.response.user.GoogleProfileResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        given(serverTimeManager.getDateAndTime())
                .willReturn(LocalDateTime.now());

        // when
        final ValidatableResponse response = post("/login", loginRequest);

        // then
        response.statusCode(OK.value())
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
        response.statusCode(BAD_REQUEST.value())
                .body("message", equalTo("이메일이나 비밀번호가 틀렸습니다."));
    }


    @DisplayName("구글 로그인에 성공할 때 토큰과 상태코드 200을 반환한다.")
    @Test
    void loginWithGoogle() {
        // given
        given(serverTimeManager.getDateAndTime())
                .willReturn(LocalDateTime.now());
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
        response.statusCode(OK.value())
                .cookie("refreshToken", notNullValue())
                .body("accessToken", notNullValue());
    }

    @DisplayName("Access token 재발급 요청 시 쿠키의 Refresh token을 활용해 새로운 Access token과 상태 코드 200을 반환한다.")
    @Test
    void refresh_accessToken() {
        // given
        final User user = AZPI.create();
        signUp(user);

        given(serverTimeManager.getDateAndTime())
                .willReturn(LocalDateTime.now());
        final LoginRequest loginRequest = new LoginRequest(user.getEmail(), "1234asdf!");
        final ExtractableResponse<Response> loginResponse = post("/login", loginRequest).extract();
        final Map<String, String> cookies = loginResponse.cookies();

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookies(cookies)
                .when().get("/token/refresh")
                .then().log().all();

        // then
        response.statusCode(OK.value())
                .cookie("refreshToken", notNullValue())
                .body("accessToken", notNullValue());
    }

    @DisplayName("로그아웃시 쿠키를 제거하고 상태코드 204를 반환한다.")
    @Test
    void logout() {
        // given
        final User user = SUN.create();
        signUp(user);
        given(serverTimeManager.getDateAndTime())
                .willReturn(LocalDateTime.now());
        final LoginRequest loginRequest = new LoginRequest(user.getEmail(), "1234asdf!");
        final ExtractableResponse<Response> loginResponse = post("/login", loginRequest).extract();

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookies(loginResponse.cookies())
                .when().post("/token/logout")
                .then().log().all();

        // then
        response.statusCode(NO_CONTENT.value())
                .cookie("refreshToken", "");
    }

    @DisplayName("Access token 재발급 요청 시 쿠키에 Refresh token이 존재하지 않는다면 상태 코드 401과 tokenStatus를 empty로 반환한다.")
    @Test
    void refresh_accessToken_throwsException_ifRefreshTokenAbsent() {
        // given
        final Map<String, String> cookies = Map.of("key", "value");

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookies(cookies)
                .when().get("/token/refresh")
                .then().log().all();

        // then
        response.statusCode(UNAUTHORIZED.value())
                .body("tokenStatus", equalTo("empty"));
    }
}
