package com.woowacourse.moragora.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.woowacourse.moragora.dto.UserRequest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("회원 관련 기능")
public class UserAcceptanceTest extends AcceptanceTest {

    @DisplayName("이메일, 비밀번호, 닉네임 모든 양식에 맞게 작성하면 회원가입에 성공하고 상태코드 201을 반환받는다.")
    @Test
    void signUp() {
        // given
        final UserRequest userRequest = new UserRequest("kun@naver.com", "1234smart!", "kun");

        // when
        final ValidatableResponse response = post("/users", userRequest);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", notNullValue());
    }

    @DisplayName("가입되지 않은 이메일에 대해 중복 확인을 요청하면 false와 상태코드 200을 반환받는다.")
    @Test
    void checkEmail_ifExists() {
        // given
        final String email = "someUniqueEmail@unique.email";

        // when
        final ValidatableResponse response = get("/users/check-email?email=\"" + email + "\"");

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("isExist", equalTo(false));
    }

    @DisplayName("존재하는 이메일에 대해 중복 확인을 요청하면 true와 상태코드 200을 반환받는다.")
    @Test
    void checkEmail_IfNotExist() {
        // given
        final String email = "kun@naver.com";
        final UserRequest userRequest = new UserRequest(email, "1234smart!", "kun");
        post("/users", userRequest);

        // when
        final ValidatableResponse response = get("/users/check-email?email=" + email);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("isExist", equalTo(true));
    }
}
