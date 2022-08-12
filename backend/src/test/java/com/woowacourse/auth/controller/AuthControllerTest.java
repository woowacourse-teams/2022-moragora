package com.woowacourse.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.auth.dto.LoginResponse;
import com.woowacourse.auth.exception.AuthorizationFailureException;
import com.woowacourse.moragora.controller.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class AuthControllerTest extends ControllerTest {

    @DisplayName("로그인에 성공한다.")
    @Test
    void login() throws Exception {
        // given
        final String email = "kun@email.com";
        final String password = "1234asdfg!";
        final LoginRequest loginRequest = new LoginRequest(email, password);
        final String accessToken = "fake_token";

        given(authService.createToken(any(LoginRequest.class)))
                .willReturn(new LoginResponse(accessToken));

        // when
        final ResultActions resultActions = performPost("/login", loginRequest);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").value(accessToken))
                .andDo(document("auth/login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description(email),
                                fieldWithPath("password").type(JsonFieldType.STRING).description(password)
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description(accessToken)
                        )
                ));
    }

    @DisplayName("로그인에 실패한다.")
    @Test
    void login_fail() throws Exception {
        // given
        final String email = "kun@email.com";
        final String password = "1234asdfa!!";
        final LoginRequest loginRequest = new LoginRequest(email, password);

        given(authService.createToken(any(LoginRequest.class)))
                .willThrow(new AuthorizationFailureException());
        final String message = "이메일이나 비밀번호가 틀렸습니다.";

        // when
        final ResultActions resultActions = performPost("/login", loginRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(message))
                .andDo(document("auth/login-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description(email),
                                fieldWithPath("password").type(JsonFieldType.STRING).description(password)
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description(message)
                        )
                ));
    }
}
