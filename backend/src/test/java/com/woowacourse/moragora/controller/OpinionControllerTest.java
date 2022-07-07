package com.woowacourse.moragora.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.moragora.dto.OpinionRequest;
import com.woowacourse.moragora.exception.DiscussionNotFoundException;
import com.woowacourse.moragora.service.OpinionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {OpinionController.class})
class OpinionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OpinionService opinionService;

    @DisplayName("게시글에 의견을 생성한다.")
    @Test
    void add() throws Exception {
        // given
        final OpinionRequest opinionRequest = new OpinionRequest("첫 번째 의견");

        // when
        given(opinionService.save(anyLong(), any(OpinionRequest.class)))
                .willReturn(1L);

        // then
        mockMvc.perform(post("/discussions/1/opinions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opinionRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("/discussions/1/opinions/" + 1)));
    }

    @DisplayName("존재하지 않는 토론 게시글에 의견을 등록하면 예외가 발생한다.")
    @Test
    void save_throwsException_ifDiscussionNotFound() throws Exception {
        // given
        final OpinionRequest opinionRequest = new OpinionRequest("첫 번째 의견");
        given(opinionService.save(eq(1L), any(OpinionRequest.class)))
                .willThrow(new DiscussionNotFoundException());

        // when, then
        mockMvc.perform(post("/discussions/1/opinions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opinionRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("토론 게시글이 존재하지 않습니다.")));
    }

}
