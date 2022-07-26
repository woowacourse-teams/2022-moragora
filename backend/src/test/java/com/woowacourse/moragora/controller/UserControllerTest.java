package com.woowacourse.moragora.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.moragora.dto.EmailCheckResponse;
import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.dto.UserResponse;
import com.woowacourse.moragora.dto.UsersResponse;
import com.woowacourse.moragora.exception.NoParameterException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class UserControllerTest extends ControllerTest {

    @DisplayName("회원가입에 성공한다.")
    @Test
    void signUp() throws Exception {
        // given
        final String email = "kun@email.com";
        final String password = "1234Asdfg!";
        final String nickname = "kun";
        final UserRequest userRequest = new UserRequest(email, password, nickname);

        given(userService.create(any(UserRequest.class)))
                .willReturn(1L);

        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/users", userRequest);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("/users/" + 1)))
                .andDo(document("user/sign-up",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description(email),
                                fieldWithPath("password").type(JsonFieldType.STRING).description(password),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description(nickname)
                        )
                ));
    }

    @DisplayName("회원 가입 양식이 잘못된 경우 예외를 발생한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "kun,1234pass!,kun",
            "kun@email.com,1234pass,kun",
            "kun@email.com,1234!!!!,쿤",
            "kun@email.com,pass!!!!,kun",
            "kun@email.com,pass1!,kun",
            "kun@email.com,1234pass!,kunasdf!!!!",
            "kun@email.com,1234pass!,forkyforkyforkyy",
            "kun@email.com,1234pass!,forky forky",
            "kun@a,1234adsfg!,kun",
    })
    void signUp_throwsException_ifInputValueIsInvalid(final String email, final String password, final String nickname)
            throws Exception {
        // given
        final UserRequest userRequest = new UserRequest(email, password, nickname);

        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/users", userRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("입력 형식이 올바르지 않습니다."));
    }

    @DisplayName("이메일의 중복 여부를 확인한다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void checkEmail(final boolean isExist) throws Exception {
        // given
        final String email = "kun@email.com";
        given(userService.isEmailExist(email))
                .willReturn(new EmailCheckResponse(isExist));

        // when
        final ResultActions resultActions = performGet("/users/check-email?email=" + email);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.isExist").value(isExist))
                .andDo(document("user/check-duplicate-email",
                        responseFields(
                                fieldWithPath("isExist").type(JsonFieldType.BOOLEAN).description(isExist)
                        )
                ));
    }

    @DisplayName("이메일을 입력하지 않고 중복 여부를 확인하면 예외가 발생한다.")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {" "})
    void checkEmail_throwsException_ifBlank(final String email) throws Exception {
        // given
        given(userService.isEmailExist(email))
                .willThrow(new NoParameterException());

        // when
        final ResultActions resultActions = performGet("/users/check-email?email=" + email);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("값이 입력되지 않았습니다."));
    }

    @DisplayName("keyword로 유저를 검색해 반환한다.")
    @Test
    void search() throws Exception {
        // given
        final String keyword = "foo";

        final UsersResponse usersResponse = new UsersResponse(
                List.of(
                        new UserResponse(1L, "aaa111@foo.com", "아스피"),
                        new UserResponse(2L, "bbb222@foo.com", "필즈"),
                        new UserResponse(3L, "ccc333@foo.com", "포키"),
                        new UserResponse(4L, "ddd444@foo.com", "썬"),
                        new UserResponse(5L, "eee555@foo.com", "우디"),
                        new UserResponse(6L, "fff666@foo.com", "쿤"),
                        new UserResponse(7L, "ggg777@foo.com", "반듯"))
        );

        given(userService.searchByKeyword(keyword))
                .willReturn(usersResponse);

        // when
        final ResultActions resultActions = performGet("/users?keyword=" + keyword);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.users[*].id", containsInAnyOrder(1, 2, 3, 4, 5, 6, 7)))
                .andExpect(jsonPath("$.users[*].email", containsInAnyOrder(
                        "aaa111@foo.com",
                        "bbb222@foo.com",
                        "ccc333@foo.com",
                        "ddd444@foo.com",
                        "eee555@foo.com",
                        "fff666@foo.com",
                        "ggg777@foo.com")))
                .andExpect(jsonPath("$.users[*].nickname", containsInAnyOrder(
                        "아스피", "필즈", "포키", "썬", "우디", "쿤", "반듯")))
                .andDo(document("user/keyword-search",
                        responseFields(
                                fieldWithPath("users[].id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("users[].email").type(JsonFieldType.STRING).description("aaa111@foo.com"),
                                fieldWithPath("users[].nickname").type(JsonFieldType.STRING).description("아스피")
                        )
                ));
    }

    @DisplayName("keyword를 입력하지 않고 검색하면 예외가 발생한다.")
    @Test
    void search_throwsException_ifNoKeyword() throws Exception {
        // given
        final String keyword = "";
        given(userService.searchByKeyword(keyword))
                .willThrow(new NoParameterException());

        // when
        final ResultActions resultActions = performGet("/users?keyword=" + keyword);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("값이 입력되지 않았습니다.")));
    }

    @DisplayName("로그인한 회원의 정보를 조회한다.")
    @Test
    void findMe() throws Exception {
        // given
        final long id = 1L;
        final String email = "foo@email.com";
        final String nickname = "foo";
        final UserResponse userResponse = new UserResponse(id, email, nickname);

        validateToken("1");
        given(userService.findById(id))
                .willReturn(userResponse);

        // when
        final ResultActions resultActions = performGet("/users/me");

        // then
        resultActions
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("nickname").value(nickname))
                .andDo(document("user/find-my-info",
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("foo@email.com"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("foo")
                        )
                ));
    }
}
