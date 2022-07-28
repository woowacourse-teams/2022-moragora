package com.woowacourse.moragora.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class AttendanceControllerTest extends ControllerTest {


    @DisplayName("출석을 제출하려는 방이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void markAttendance_throwsException_ifMeetingNotFound() throws Exception {
        // given
        final Long meetingId = 99L;
        final Long userId = 1L;
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        validateToken("1");

        doThrow(new MeetingNotFoundException())
                .when(attendanceService)
                .updateAttendance(anyLong(), anyLong(), any(UserAttendanceRequest.class));

        // when
        final ResultActions resultActions = performPut("/meetings/" + meetingId + "/users/" + userId, request);

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @DisplayName("사용자 출석여부를 반영한다.")
    @Test
    void markAttendance() throws Exception {
        // given
        final Long meetingId = 1L;
        final Long userId = 1L;
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        validateToken("1");

        // when
        final ResultActions resultActions = performPut("/meetings/" + meetingId + "/users/" + userId, request);

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(document("meeting/enter-Attendance",
                        requestFields(
                                fieldWithPath("attendanceStatus").type(JsonFieldType.STRING).description("present")
                        )
                ));
    }

    @DisplayName("출석을 제출하려는 사용자가 미팅에 존재하지 않으면 예외가 발생한다.")
    @Test
    void markAttendance_throwsException_ifParticipantNotFound() throws Exception {
        // given
        final Long meetingId = 1L;
        final Long userId = 8L;
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        validateToken("1");

        doThrow(new ParticipantNotFoundException())
                .when(attendanceService)
                .updateAttendance(anyLong(), anyLong(), any(UserAttendanceRequest.class));

        // when
        final ResultActions resultActions = performPut("/meetings/" + meetingId + "/users/" + userId, request);

        // then
        resultActions.andExpect(status().isNotFound());
    }
}
