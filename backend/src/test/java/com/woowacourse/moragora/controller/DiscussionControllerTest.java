package com.woowacourse.moragora.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.moragora.dto.DiscussionRequest;
import com.woowacourse.moragora.service.DiscussionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
}
