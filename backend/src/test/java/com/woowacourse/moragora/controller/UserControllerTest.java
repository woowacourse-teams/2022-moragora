package com.woowacourse.moragora.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.auth.support.JwtTokenProvider;
import com.woowacourse.moragora.dto.EmailCheckResponse;
import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.dto.UserResponse2;
import com.woowacourse.moragora.dto.UsersResponse;
import com.woowacourse.moragora.exception.NoKeywordException;
import com.woowacourse.moragora.exception.NoParameterException;
import com.woowacourse.moragora.service.UserService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {UserController.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("회원가입에 성공한다.")
    @Test
    void signUp() throws Exception {
        // given
        final UserRequest userRequest = new UserRequest("kun@naver.com", "1234smart!", "kun");
        given(userService.create(any(UserRequest.class)))
                .willReturn(1L);

        // when
        given(jwtTokenProvider.validateToken(any()))
                .willReturn(true);
        given(jwtTokenProvider.getPayload(any()))
                .willReturn("1");

        // then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("/users/" + 1)));
    }

    @DisplayName("회원 가입 양식이 잘못된 경우 예외를 발생한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "kun,1234smart!,kun",
            "kun@email.com,1234smart,kun",
            "kun@email.com,1234!!!!,쿤",
            "kun@email.com,smart!!!!,kun",
            "kun@email.com,smart1!,kun",
            "kun@email.com,1234smart!,kunasdf!!!!",
            "kun@email.com,1234smart!,forkyforkyforkyy",
            "kun@email.com,1234smart!,forky forky",
            "kun@a,1234smart!,kun",
    })
    void signUp_throwsException_ifInputValueIsInvalid(final String email, final String password, final String nickname)
            throws Exception {
        // given
        final UserRequest userRequest = new UserRequest(email, password, nickname);

        // when
        given(jwtTokenProvider.validateToken(any()))
                .willReturn(true);
        given(jwtTokenProvider.getPayload(any()))
                .willReturn("1");

        // then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("입력 형식이 올바르지 않습니다."));
    }

    @DisplayName("이메일의 중복 여부를 확인한다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void checkEmail(final boolean isExist) throws Exception {
        // given
        final String email = "kun@naver.com";
        given(userService.isEmailExist(email))
                .willReturn(new EmailCheckResponse(isExist));

        // when, then
        mockMvc.perform(get("/users/check-email?email=" + email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isExist").value(isExist));
    }

    @DisplayName("이메일을 입력하지 않고 중복 여부를 확인하면 예외가 발생한다.")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {" "})
    void checkEmail_throwsException_ifBlank(final String email) throws Exception {
        // given
        given(userService.isEmailExist(email))
                .willThrow(new NoParameterException());

        // when, then
        mockMvc.perform(get("/users/check-email?email=" + email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("값이 입력되지 않았습니다."));
    }

    @DisplayName("keyword로 유저를 검색해 반환한다.")
    @Test
    void search() throws Exception {
        // given
        final String keyword = "foo";
        given(userService.searchByKeyword(keyword))
                .willReturn(new UsersResponse(
                        List.of(
                                new UserResponse2(1L, "aaa111@foo.com", "아스피"),
                                new UserResponse2(2L, "bbb222@foo.com", "필즈"),
                                new UserResponse2(3L, "ccc333@foo.com", "포키"),
                                new UserResponse2(4L, "ddd444@foo.com", "썬"),
                                new UserResponse2(5L, "eee555@foo.com", "우디"),
                                new UserResponse2(6L, "fff666@foo.com", "쿤"),
                                new UserResponse2(7L, "ggg777@foo.com", "반듯"))
                ));

        // when, then
        mockMvc.perform(get("/users?keyword=" + keyword)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
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
                        "아스피", "필즈", "포키", "썬", "우디", "쿤", "반듯")));
    }

    @DisplayName("keyword를 입력하지 않고 검색하면 예외가 발생한다.")
    @Test
    void search_throwsException_ifNoKeyword() throws Exception {
        // given
        final String keyword = "";
        given(userService.searchByKeyword(keyword))
                .willThrow(new NoKeywordException());

        // when, then
        mockMvc.perform(get("/users?keyword=" + keyword)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("검색어가 입력되지 않았습니다.")));
    }

    @DisplayName("로그인한 회원의 정보를 조회한다.")
    @Test
    void findMe() throws Exception {
        // given
        final long id = 1L;
        final String email = "foo@email.com";
        final String nickname = "foo";
        final UserResponse2 userResponse2 = new UserResponse2(id, email, nickname);

        given(userService.findById(id))
                .willReturn(userResponse2);

        // when
        given(jwtTokenProvider.validateToken(any()))
                .willReturn(true);
        given(jwtTokenProvider.getPayload(any()))
                .willReturn("1");

        // then
        mockMvc.perform(get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("nickname").value(nickname));
    }
}
