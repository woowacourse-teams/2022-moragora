package com.woowacourse.moragora.presentation;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.moragora.constant.SessionAttributeNames;
import com.woowacourse.moragora.dto.request.user.NicknameRequest;
import com.woowacourse.moragora.dto.request.user.PasswordRequest;
import com.woowacourse.moragora.dto.request.user.UserDeleteRequest;
import com.woowacourse.moragora.dto.request.user.UserRequest;
import com.woowacourse.moragora.dto.response.user.UserResponse;
import com.woowacourse.moragora.dto.response.user.UsersResponse;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.global.NoParameterException;
import com.woowacourse.moragora.exception.user.InvalidPasswordException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends ControllerTest {

    @DisplayName("회원가입에 성공한다.")
    @Test
    void signUp() throws Exception {
        // given
        final String email = "kun@email.com";
        final String password = "1234Asdfg!";
        final String nickname = "kun";
        final UserRequest userRequest = new UserRequest(email, password, nickname);
        final MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionAttributeNames.VERIFIED_EMAIL, email);

        given(userService.create(any(UserRequest.class), eq(email)))
                .willReturn(1L);

        validateToken("1");

        // when
        final ResultActions resultActions = performPostWithSession("/users", userRequest, session);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("/users/" + 1)))
                .andDo(document("user/sign-up",
                        preprocessRequest(prettyPrint()),
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

    @DisplayName("인증되지않은 이메일로 회원가입을 할 경우 예외가 발생한다.")
    @Test
    void signUp_throwsException_ifNotVerifiedEmail() throws Exception {
        // given
        final String email = "kun@email.com";
        final String password = "1234Asdfg!";
        final String nickname = "kun";
        final UserRequest userRequest = new UserRequest(email, password, nickname);

        given(userService.create(any(UserRequest.class), eq(null)))
                .willThrow(new ClientRuntimeException("인증되지 않은 이메일입니다.", HttpStatus.BAD_REQUEST));

        validateToken("1");

        // when
        final ResultActions resultActions = performPost("/users", userRequest);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", equalTo("인증되지 않은 이메일입니다.")));
    }

    @DisplayName("keyword로 유저를 검색해 반환한다.")
    @Test
    void search() throws Exception {
        // given
        final String keyword = "foo";

        final UsersResponse usersResponse = new UsersResponse(
                List.of(
                        new UserResponse(1L, "aaa111@foo.com", "아스피", "checkmate"),
                        new UserResponse(2L, "bbb222@foo.com", "필즈", "checkmate"),
                        new UserResponse(3L, "ccc333@foo.com", "포키", "checkmate"),
                        new UserResponse(4L, "ddd444@foo.com", "썬", "checkmate"),
                        new UserResponse(5L, "eee555@foo.com", "우디", "checkmate"),
                        new UserResponse(6L, "fff666@foo.com", "쿤", "checkmate"),
                        new UserResponse(7L, "ggg777@foo.com", "반듯", "checkmate"))
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
                .andExpect(jsonPath("$.users[*].authProvider", containsInAnyOrder(
                        "checkmate", "checkmate", "checkmate", "checkmate", "checkmate", "checkmate", "checkmate")))
                .andDo(document("user/keyword-search",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("users[].id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("users[].email").type(JsonFieldType.STRING).description("aaa111@foo.com"),
                                fieldWithPath("users[].nickname").type(JsonFieldType.STRING).description("아스피"),
                                fieldWithPath("users[].authProvider").type(JsonFieldType.STRING)
                                        .description("checkmate")
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
        final String authProvider = "checkmate";
        final UserResponse userResponse = new UserResponse(id, email, nickname, authProvider);

        validateToken("1");
        given(userService.findById(id))
                .willReturn(userResponse);

        // when
        final ResultActions resultActions = performGet("/users/me");

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("nickname").value(nickname))
                .andExpect(jsonPath("authProvider").value(authProvider))
                .andDo(document("user/find-my-info",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description(1L),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("foo@email.com"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("foo"),
                                fieldWithPath("authProvider").type(JsonFieldType.STRING).description("checkmate")
                        )
                ));
    }

    @DisplayName("로그인하지 않은 상태에서 회원의 정보를 조회하면 예외가 발생한다.")
    @Test
    void findMe_ifNotLoggedIn() throws Exception {
        // given, when
        final ResultActions resultActions = performGet("/users/me");

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andDo(document("user/find-my-info-unauthorized"));
    }

    @DisplayName("로그인한 회원의 닉네임을 수정한다.")
    @Test
    void changeMyNickname() throws Exception {
        // given
        validateToken("1");
        final String nickname = "반듯";
        final NicknameRequest request = new NicknameRequest(nickname);

        // when
        final ResultActions resultActions = performPut("/users/me/nickname", request);

        // then
        verify(userService, times(1)).updateNickname(any(NicknameRequest.class), any(Long.class));
        resultActions.andExpect(status().isNoContent())
                .andDo(document("user/change-my-nickname",
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description(nickname)
                        )
                ));
    }

    @DisplayName("형식에 맞지 않는 닉네임으로 수정하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"반_듯", "멋쟁이프론트개발자우리의자랑밧드", ""})
    void changeMyNickname_throwsException_ifInvalidNickname(final String nickname) throws Exception {
        // given
        validateToken("1");
        final NicknameRequest request = new NicknameRequest(nickname);

        // when
        final ResultActions resultActions = performPut("/users/me/nickname", request);

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("로그인한 회원의 비밀번호를 수정한다.")
    @Test
    void changeMyPassword() throws Exception {
        // given
        validateToken("1");
        final String oldPassword = "1234asdf!";
        final String newPassword = "new1234!";
        final PasswordRequest request = new PasswordRequest(oldPassword, newPassword);

        // when
        final ResultActions resultActions = performPut("/users/me/password", request);

        // then
        verify(userService, times(1)).updatePassword(any(PasswordRequest.class), any(Long.class));
        resultActions.andExpect(status().isNoContent())
                .andDo(document("user/change-my-password",
                        requestFields(
                                fieldWithPath("oldPassword").type(JsonFieldType.STRING).description(oldPassword),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING).description(newPassword)
                        )
                ));
    }

    @DisplayName("기존 비밀번호와 같은 비밀번호로 비밀번호를 수정하면 예외가 발생한다.")
    @Test
    void changeMyPassword_throwsException_ifSamePassword() throws Exception {
        // given
        validateToken("1");
        final String oldPassword = "1234asdf!";
        final String newPassword = "1234asdf!";
        final PasswordRequest request = new PasswordRequest(oldPassword, newPassword);

        doThrow(new ClientRuntimeException("새로운 비밀번호가 기존의 비밀번호와 일치합니다.", HttpStatus.BAD_REQUEST))
                .when(userService)
                .updatePassword(any(PasswordRequest.class), anyLong());

        // when
        final ResultActions resultActions = performPut("/users/me/password", request);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(document("user/change-my-password-same-as-is",
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("새로운 비밀번호가 기존의 비밀번호와 일치합니다.")
                        )
                ));
    }

    @DisplayName("형식에 맞지 않는 비밀번호로 비밀번호를 수정하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"new1234", "12345678!", "newpass!", "newpw1!", "123456789a123456789a123456789a!"})
    void changeMyPassword_throwsException_ifInvalidPassword(final String password) throws Exception {
        // given
        validateToken("1");
        final String oldPassword = "1234asdf!";
        final PasswordRequest request = new PasswordRequest(oldPassword, password);

        // when
        final ResultActions resultActions = performPut("/users/me/password", request);

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("기존 비밀번호를 틀리게 입력하고 비밀번호를 수정하면 예외가 발생한다.")
    @Test
    void changeMyPassword_throwsException_ifWrongPassword() throws Exception {
        // given
        validateToken("1");
        final String oldPassword = "1234wrong!";
        final String newPassword = "1234asdf!";
        final PasswordRequest request = new PasswordRequest(oldPassword, newPassword);

        doThrow(new InvalidPasswordException())
                .when(userService)
                .updatePassword(any(PasswordRequest.class), anyLong());

        // when
        final ResultActions resultActions = performPut("/users/me/password", request);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(document("user/change-my-password-wrong-password",
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("비밀번호가 올바르지 않습니다.")
                        )
                ));
    }

    @DisplayName("로그인한 회원을 탈퇴시킨다.")
    @Test
    void deleteMe() throws Exception {
        // given
        final UserDeleteRequest userDeleteRequest = new UserDeleteRequest("1234asdf!");
        validateToken("1");

        // when
        final ResultActions resultActions = performDelete("/users/me", userDeleteRequest);

        // then
        verify(userService, times(1)).delete(any(UserDeleteRequest.class), anyLong());
        resultActions.andExpect(status().isNoContent())
                .andDo(document("user/delete-me",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("1234asdf!")
                        )
                ));
    }

    @DisplayName("로그인한 회원이 마스터인 모임이 있으면 예외가 발생한다.")
    @Test
    void deleteMe_throwsException_ifMaster() throws Exception {
        // given
        final UserDeleteRequest userDeleteRequest = new UserDeleteRequest("1234asdf!");
        validateToken("1");

        doThrow(new ClientRuntimeException("마스터로 참여중인 모임이 있어 탈퇴할 수 없습니다.", HttpStatus.FORBIDDEN))
                .when(userService).delete(any(UserDeleteRequest.class), anyLong());
        // when
        final ResultActions resultActions = performDelete("/users/me", userDeleteRequest);

        // then
        resultActions.andExpect(status().isForbidden());
    }
}
