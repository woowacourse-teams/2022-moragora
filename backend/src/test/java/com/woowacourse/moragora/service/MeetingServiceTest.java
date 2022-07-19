package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.exception.ClosingTimeExcessException;
import com.woowacourse.moragora.exception.MeetingNotFoundException;
import com.woowacourse.moragora.exception.ParticipantNotFoundException;
import com.woowacourse.moragora.service.closingstrategy.ServerTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MeetingServiceTest {

    @Autowired
    private MeetingService meetingService;

    @MockBean
    private ServerTime serverTime;

    @DisplayName("미팅 방을 저장한다.")
    @Test
    void save() {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)
        );

        // when
        final Long expected = meetingService.save(meetingRequest, 1L);

        // then
        assertThat(expected).isNotNull();
    }

    @DisplayName("미팅 방을 저장한다.")
    @Test
    void save2() {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(2L, 3L, 4L, 5L, 6L, 7L)
        );

        // when
        final Long expected = meetingService.save(meetingRequest, 1L);

        // then
        assertThat(expected).isNotNull();
    }

    // TODO userResponse 테스트 작성
    @DisplayName("id로 모임 상세 정보를 조회한다.")
    @Test
    void findById() {
        // given
        final Long id = 1L;
        final MeetingResponse expectedMeetingResponse = new MeetingResponse(
                1L,
                "모임1",
                0,
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                null
        );

        // when
        final MeetingResponse response = meetingService.findById(id, 1L);

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("users")
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("사용자들의 출석 여부를 변경한다.")
    @Test
    void updateAttendance() {
        // given
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);
        final Long meetingId = 1L;
        final Long userId = 1L;

        // when, then
        assertThatCode(() -> meetingService.updateAttendance(meetingId, userId, request, 1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("사용자의 출석 여부를 변경하려고 할 때, 미팅방이 존재하지 않는다면 예외가 발생한다. ")
    @Test
    void updateAttendance_throwsException_ifMeetingNotFound() {
        // given
        final Long meetingId = 999L;
        final Long userId = 1L;
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        // when, then
        assertThatThrownBy(() -> meetingService.updateAttendance(meetingId, userId, request, 1L))
                .isInstanceOf(MeetingNotFoundException.class);
    }

    @DisplayName("사용자의 출석 여부를 변경하려고 할 때, 미팅 참가자가 존재하지 않는다면 예외가 발생한다. ")
    @Test
    void updateAttendance_throwsException_ifParticipantNotFound() {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest("meeting", LocalDate.now(), LocalDate.now(),
                LocalTime.now(),
                LocalTime.now(), List.of(2L, 3L, 4L, 5L));
        final Long meetingId = meetingService.save(meetingRequest, 1L);
        final Long userId = 6L;

        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        // when, then
        assertThatThrownBy(() -> meetingService.updateAttendance(meetingId, userId, request, 1L))
                .isInstanceOf(ParticipantNotFoundException.class);
    }

    @DisplayName("사용자 출석 제출 시간이 마감 시간을 초과할 경우 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifDeadlineTimeExcess() {
        // given
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        given(serverTime.isExcessClosingTime(any(LocalTime.class), any(LocalTime.class)))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> meetingService.updateAttendance(1L, 1L, request, 1L))
                .isInstanceOf(ClosingTimeExcessException.class);
    }
}
