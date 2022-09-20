package com.woowacourse.moragora.presentation;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.moragora.dto.response.ServerTimeResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class CommonControllerTest extends ControllerTest {

    @DisplayName("서버의 현재 시간을 조회한다.")
    @Test
    void showServerTime() throws Exception {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final Timestamp expected = Timestamp.valueOf(now);

        given(commonService.getServerTime())
                .willReturn(new ServerTimeResponse(now));

        // when
        final ResultActions resultActions = performGet("/server-time");

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("serverTime").value(expected.getTime()))
                .andDo(document("common/server-time",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("serverTime").type(JsonFieldType.NUMBER).description(expected.getTime())
                        )
                ));
    }
}
