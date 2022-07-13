package com.woowacourse.moragora.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @DisplayName("회원가입에 성공한다.")
    @Test
    void signUp() throws Exception {
        // given
        final UserRequest userRequest = new UserRequest("kun@naver.com", "1234smart!", "kun");
        given(userService.create(any(UserRequest.class)))
                .willReturn(1L);

        // when, then
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

        // when, then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("입력 형식이 올바르지 않습니다."));
    }
}
