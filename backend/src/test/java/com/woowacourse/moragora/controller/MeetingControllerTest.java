package com.woowacourse.moragora.controller;

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
import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.service.MeetingService;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {MeetingController.class})
class MeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MeetingService meetingService;

    @DisplayName("미팅 방을 생성한다.")
    @Test
    void add() throws Exception {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)
        );

        // when
        given(meetingService.save(any(MeetingRequest.class)))
                .willReturn(1L);

        // then
        mockMvc.perform(post("/meetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetingRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("/meetings/" + 1)));
    }

    @DisplayName("단일 미팅 방을 조회한다.")
    @Test
    void findOne() throws Exception {
        // given
        final MeetingResponse meetingResponse = new MeetingResponse(
                1L,
                "모임1",
                0,
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0));

        // when
        given(meetingService.findById(1L))
                .willReturn(meetingResponse);

        // then
        mockMvc.perform(get("/meetings/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("모임1")))
                .andExpect(jsonPath("$.attendanceCount", equalTo(0)))
                .andExpect(jsonPath("$.startDate", equalTo("2022-07-10")))
                .andExpect(jsonPath("$.endDate", equalTo("2022-08-10")))
                .andExpect(jsonPath("$.entranceTime", equalTo("10:00:00")))
                .andExpect(jsonPath("$.leaveTime", equalTo("18:00:00")));
    }
}