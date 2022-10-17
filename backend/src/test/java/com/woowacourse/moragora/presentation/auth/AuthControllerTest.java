package com.woowacourse.moragora.presentation.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import com.woowacourse.moragora.presentation.ControllerTest;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class AuthControllerTest extends ControllerTest {

    @DisplayName("로그인에 성공한다.")
    @Test
    void login() throws Exception {
        // given
        final String email = "kun@email.com";
        final String password = "1234asdfg!";
        final LoginRequest loginRequest = new LoginRequest(email, password);
        final String accessToken = "fake_access_token";
        final String refreshToken = "fake_refresh_token";

        given(authService.login(any(LoginRequest.class)))
                .willReturn(new TokenResponse(accessToken, refreshToken));
        given(refreshTokenCookieProvider.create(refreshToken))
                .willReturn(ResponseCookie.from("refreshToken", refreshToken).build());

        // when
        final ResultActions resultActions = performPost("/login", loginRequest);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").value(accessToken))
                .andExpect(cookie().value("refreshToken", refreshToken))
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

        given(authService.login(any(LoginRequest.class)))
                .willThrow(new AuthenticationFailureException());
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

    @DisplayName("구글 로그인에 성공한다.")
    @Test
    void loginWithGoogle() throws Exception {
        // given
        final String accessToken = "fake_access_token";
        final String refreshToken = "fake_refresh_token";

        given(authService.loginWithGoogle(anyString()))
                .willReturn(new TokenResponse(accessToken, refreshToken));
        given(refreshTokenCookieProvider.create(refreshToken))
                .willReturn(ResponseCookie.from("refreshToken", refreshToken).build());

        // when
        final ResultActions resultActions = performPost("/login/oauth2/google?code=any");

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").value(accessToken))
                .andExpect(cookie().value("refreshToken", refreshToken))
                .andDo(document("auth/login-google",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description(accessToken)
                        )));
    }

    @DisplayName("Access token을 재발급한다.")
    @Test
    void refresh_accessToken() throws Exception {
        // given
        final String newAccessToken = "new_access_token";
        final String newRefreshToken = "new_refresh_token";

        given(refreshTokenCookieProvider.extractRefreshToken(any()))
                .willReturn("refresh_token");
        given(authService.refreshTokens("refresh_token"))
                .willReturn(new TokenResponse(newAccessToken, newRefreshToken));
        given(refreshTokenCookieProvider.create(newRefreshToken))
                .willReturn(ResponseCookie.from("refreshToken", newRefreshToken).build());

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/token/refresh")
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", "refresh_token")))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").value(newAccessToken))
                .andExpect(cookie().value("refreshToken", newRefreshToken))
                .andDo(document("auth/refresh-tokens",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description(newAccessToken)
                        )));
    }


    @DisplayName("로그아웃에 성공한다.")
    @Test
    void logout() throws Exception {
        // given
        given(refreshTokenCookieProvider.createInvalidCookie())
                .willReturn(ResponseCookie.from("refreshToken", "").build());

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/token/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", "refresh_token")))
                .andDo(print());

        // then
        resultActions.andExpect(status().isNoContent());
    }
}
