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
import com.woowacourse.moragora.infrastructure.MailSender;
import com.woowacourse.moragora.presentation.auth.AuthController;
import com.woowacourse.moragora.support.JwtTokenProvider;
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
        EventController.class,
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
    protected EventService eventService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected CommonService commonService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected QueryCountInspector queryCountInspector;

    @MockBean
    protected MailSender mailSender;

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
        given(jwtTokenProvider.validateToken(any()))
                .willReturn(true);
        given(jwtTokenProvider.getPayload(any()))
                .willReturn(id);
        return Long.valueOf(id);
    }
}
