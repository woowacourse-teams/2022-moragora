package com.woowacourse.moragora.presentation.auth;

import static com.woowacourse.moragora.constant.SessionAttributeNames.AUTH_CODE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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

import com.woowacourse.moragora.domain.auth.AuthCode;
import com.woowacourse.moragora.dto.request.auth.EmailRequest;
import com.woowacourse.moragora.dto.request.auth.EmailVerifyRequest;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.response.auth.ExpiredTimeResponse;
import com.woowacourse.moragora.dto.response.user.LoginResponse;
import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import com.woowacourse.moragora.exception.user.EmailDuplicatedException;
import com.woowacourse.moragora.presentation.ControllerTest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class AuthControllerTest extends ControllerTest {

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
        final String email = "kun@email.com";
        final String password = "1234asdfg!";
        final LoginRequest loginRequest = new LoginRequest(email, password);
        final String accessToken = "fake_token";

        given(authService.loginWithGoogle(anyString()))
                .willReturn(new LoginResponse(accessToken));

        // when
        final ResultActions resultActions = performPost("/login/oauth2/google?code=any");

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").value(accessToken))
                .andDo(document("auth/login-google",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description(accessToken)
                        )));
    }

    @DisplayName("이메일 인증번호를 전송하고 만료시간을 보내준다.")
    @Test
    void sendEmail() throws Exception {
        // given
        final String email = "ghd700@daum.net";
        final LocalDateTime expiredDate = LocalDateTime.of(2022, 10, 12, 13, 0);
        final long expiredTime = Timestamp.valueOf(expiredDate.plusMinutes(5)).getTime();

        final AuthCode authCode = new AuthCode(email, "123456", expiredDate);
        final ExpiredTimeResponse response = new ExpiredTimeResponse(expiredTime);

        given(authService.sendAuthCode(any(EmailRequest.class)))
                .willReturn(authCode);

        // when
        final ResultActions resultActions = performPost("/email/send", new EmailRequest(email));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("expiredTime").value(response.getExpiredTime()))
                .andDo(document("auth/email-send",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("expiredTime").type(JsonFieldType.NUMBER)
                                        .description(response.getExpiredTime())
                        )));
    }

    @DisplayName("중복된 이메일로 인증번호 전송을 요청하면 예외가 발생한다.")
    @Test
    void sendEmail_throwsException_whenEmailDuplicated() throws Exception {
        // given
        final String email = "ghd700@daum.net";
        final String message = "이미 존재하는 이메일입니다.";

        given(authService.sendAuthCode(any(EmailRequest.class)))
                .willThrow(new EmailDuplicatedException());

        // when
        final ResultActions resultActions = performPost("/email/send", new EmailRequest(email));

        // then
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("message").value(message))
                .andDo(document("auth/email-send-duplicated",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description(message)
                        )));
    }

    @DisplayName("인증번호를 검증한다.")
    @Test
    void verifyCode() throws Exception {
        // given
        final String email = "ghd700@daum.net";
        final String verifyCode = "000000";
        final MockHttpSession mockHttpSession = new MockHttpSession();
        final AuthCode authCode = new AuthCode(email, verifyCode, LocalDateTime.now());

        mockHttpSession.setAttribute(AUTH_CODE, authCode);
        given(authService.verifyAuthCode(any(EmailVerifyRequest.class), eq(authCode)))
                .willReturn(email);

        // when
        final ResultActions resultActions = performPostWithSession("/email/verify",
                new EmailVerifyRequest(email, verifyCode), mockHttpSession);

        // then
        resultActions.andExpect(status().isNoContent());
    }
}
