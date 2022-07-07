package com.woowacourse.moragora.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.moragora.dto.DiscussionRequest;
import com.woowacourse.moragora.dto.DiscussionResponse;
import com.woowacourse.moragora.dto.DiscussionsResponse;
import com.woowacourse.moragora.exception.DiscussionNotFoundException;
import com.woowacourse.moragora.service.DiscussionService;
import java.util.List;
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

@WebMvcTest(controllers = {DiscussionController.class})
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


    @DisplayName("모든 게시글을 조회한다.")
    @Test
    void findAll() throws Exception {
        // given
        final DiscussionsResponse discussionsResponse = new DiscussionsResponse(List.of(
                new DiscussionResponse(1L, "제목1", "내용1", 0, 0, 0),
                new DiscussionResponse(2L, "제목2", "내용2", 0, 0, 0),
                new DiscussionResponse(3L, "제목3", "내용3", 0, 0, 0)
        ));

        // when
        given(discussionService.findAll())
                .willReturn(discussionsResponse);

        // then
        mockMvc.perform(get("/discussions")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discussions[*].id", containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$.discussions[*].title", containsInAnyOrder("제목1", "제목2", "제목3")))
                .andExpect(jsonPath("$.discussions[*].content", containsInAnyOrder("내용1", "내용2", "내용3")))
                .andExpect(jsonPath("$.discussions[*].views", containsInAnyOrder(0, 0, 0)))
                .andExpect(jsonPath("$.discussions[*].createdAt", notNullValue()))
                .andExpect(jsonPath("$.discussions[*].updatedAt", notNullValue()));
    }

    @DisplayName("단일 게시글을 조회한다.")
    @Test
    void show() throws Exception {
        // given
        final DiscussionResponse discussionResponse =
                new DiscussionResponse(1L, "제목", "내용", 1, 0, 0);

        // when
        given(discussionService.findById(1L))
                .willReturn(discussionResponse);

        // then
        mockMvc.perform(get("/discussions/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.title", equalTo("제목")))
                .andExpect(jsonPath("$.content", equalTo("내용")))
                .andExpect(jsonPath("$.views", equalTo(1)))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.updatedAt", notNullValue()));
    }

    @DisplayName("존재하지 않는 토론 게시글을 조회하면 예외가 발생한다.")
    @Test
    void show_throwsException_ifDiscussionNotFound() throws Exception {
        // given
        given(discussionService.findById(1L))
                .willThrow(new DiscussionNotFoundException());

        // when, then
        mockMvc.perform(get("/discussions/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("토론 게시글이 존재하지 않습니다.")));
    }
}
