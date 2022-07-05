package com.woowacourse.moragora.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.moragora.dto.DiscussionRequest;
import com.woowacourse.moragora.service.DiscussionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class DiscussionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DiscussionService discussionService;

    @DisplayName("게시글을 생성한다.")
    @Test
    void add() throws Exception {
        // given
        final DiscussionRequest discussionRequest = new DiscussionRequest("제목", "내용");

        // when
        given(discussionService.save(any(DiscussionRequest.class)))
                .willReturn(1L);

        // then
        mockMvc.perform(post("/discussions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discussionRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("/discussions/" + 1)));
    }

    @DisplayName("제목을 입력하지 않고 게시글을 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @EmptySource
    @NullSource
    void add_throwsException_ifTitleIsEmpty(final String title) throws Exception {
        // given
        final DiscussionRequest discussionRequest = new DiscussionRequest(title, "내용");

        // when, then
        mockMvc.perform(post("/discussions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discussionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("제목의 길이가 50자를 초과할 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"012345678901234567890123456789012345678901234567891",
            "영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영",
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija"})
    void add_throwsException_ifTitleIsTooLong(final String title) throws Exception {
        // given
        final DiscussionRequest discussionRequest = new DiscussionRequest(title, "내용");

        // when, then
        mockMvc.perform(post("/discussions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discussionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("제목의 길이는 50자를 초과할 수 없습니다."));
    }

    @DisplayName("본문을 입력하지 않고 게시글을 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @EmptySource
    @NullSource
    void add_throwsException_ifContentIsEmpty(final String content) throws Exception {
        // given
        final DiscussionRequest discussionRequest = new DiscussionRequest("제목", content);

        // when, then
        mockMvc.perform(post("/discussions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discussionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
