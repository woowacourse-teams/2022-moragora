package com.woowacourse.moragora.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.auth.controller.AuthController;
import com.woowacourse.auth.service.AuthService;
import com.woowacourse.auth.support.JwtTokenProvider;
import com.woowacourse.moragora.service.AttendanceService;
import com.woowacourse.moragora.service.CommonService;
import com.woowacourse.moragora.service.MeetingService;
import com.woowacourse.moragora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = {
        MeetingController.class,
        AttendanceController.class,
        UserController.class,
        AuthController.class,
        CommonController.class})
@AutoConfigureRestDocs
public class ControllerTest {

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MeetingService meetingService;

    @MockBean
    protected AttendanceService attendanceService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected CommonService commonService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResultActions performPost(final String uri, final Object request) throws Exception {
        return mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    protected ResultActions performPost(final String uri) throws Exception {
        return mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected ResultActions performGet(final String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected ResultActions performPut(final String uri, final Object request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    protected Long validateToken(final String id) {
        given(jwtTokenProvider.validateToken(any()))
                .willReturn(true);
        given(jwtTokenProvider.getPayload(any()))
                .willReturn(id);
        return Long.valueOf(id);
    }
}
