package com.woowacourse.moragora.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.logging.QueryCountInspector;
import com.woowacourse.moragora.application.AttendanceService;
import com.woowacourse.moragora.application.CommonService;
import com.woowacourse.moragora.application.EventService;
import com.woowacourse.moragora.application.MeetingService;
import com.woowacourse.moragora.application.UserService;
import com.woowacourse.moragora.application.auth.AuthService;
import com.woowacourse.moragora.application.auth.JwtTokenProvider;
import com.woowacourse.moragora.presentation.auth.RefreshTokenCookieProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest
@AutoConfigureRestDocs
public class ControllerTest {

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MeetingService meetingService;

    @MockBean
    protected AttendanceService attendanceService;

    @MockBean
    protected EventService eventService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected CommonService commonService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected RefreshTokenCookieProvider refreshTokenCookieProvider;

    @MockBean
    protected QueryCountInspector queryCountInspector;

    @Autowired
    protected MockMvc mockMvc;

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

    protected ResultActions performDelete(final String uri, final Object request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    protected ResultActions performDelete(final String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected Long validateToken(final String id) {
        given(jwtTokenProvider.getPayload(any()))
                .willReturn(id);
        return Long.valueOf(id);
    }
}
