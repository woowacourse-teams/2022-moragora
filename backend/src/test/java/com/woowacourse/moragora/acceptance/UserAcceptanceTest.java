package com.woowacourse.moragora.acceptance;

import static org.hamcrest.Matchers.containsInAnyOrder;
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
        final ValidatableResponse response = get("/users/check-email?email=" + email);

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

    @DisplayName("키워드를 입력하면 그 키워드가 포함된 유저 목록과 상태코드 200을 반환한다.")
    @Test
    void search() {
        // given
        final String keyword = "foo";

        // when
        final ValidatableResponse response = get("/users?keyword=" + keyword);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("users.id", containsInAnyOrder(1, 2, 3, 4, 5, 6, 7))
                .body("users.nickname", containsInAnyOrder("아스피", "필즈", "포키",
                        "썬", "우디", "쿤", "반듯"))
                .body("users.email", containsInAnyOrder("aaa111@foo.com",
                        "bbb222@foo.com",
                        "ccc333@foo.com",
                        "ddd444@foo.com",
                        "eee555@foo.com",
                        "fff666@foo.com",
                        "ggg777@foo.com"));
    }

    @DisplayName("로그인 한 상태에서 자신의 회원정보를 요청하면 회원정보와 상태코드 200을 반환받는다.")
    @Test
    void findMe() {
        // given, when
        ValidatableResponse response = get("/users/me", signUpAndGetToken());

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("email", equalTo("test@naver.com"))
                .body("nickname", equalTo("kun"));
    }
}
