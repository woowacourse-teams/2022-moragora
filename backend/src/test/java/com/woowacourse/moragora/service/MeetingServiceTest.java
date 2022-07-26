package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingsResponse;
import com.woowacourse.moragora.dto.ParticipantResponse;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.exception.ClosingTimeExcessException;
import com.woowacourse.moragora.exception.InvalidParticipantException;
import com.woowacourse.moragora.exception.MeetingNotFoundException;
import com.woowacourse.moragora.exception.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.UserNotFoundException;
import com.woowacourse.moragora.service.closingstrategy.TimeChecker;
import com.woowacourse.moragora.util.CurrentDateTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private TimeChecker timeChecker;

    @MockBean
    private CurrentDateTime currentDateTime;

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
                List.of(2L, 3L, 4L, 5L, 6L, 7L)
        );

        // when
        final Long expected = meetingService.save(meetingRequest, 1L);

        // then
        assertThat(expected).isNotNull();
    }

    @DisplayName("미팅이 생성될 때, 참가자 명단에 미팅 생성자가 있는 경우 예외를 반환한다.")
    @Test
    void save_throwException_ifUserIdsContainLoginId() {
        // given
        final Long loginId = 1L;
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(loginId, 2L, 3L, 4L, 5L, 6L, 7L)
        );

        // when, then
        assertThatThrownBy(() -> meetingService.save(meetingRequest, loginId))
                .isInstanceOf(InvalidParticipantException.class);
    }

    @DisplayName("미팅이 생성될 때, 참가자 명단에 중복이 있는 경우 예외를 반환한다.")
    @Test
    void save_throwException_ifUserIdsDuplicated() {
        // given
        final Long duplicatedId = 2L;
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(duplicatedId, duplicatedId, 3L, 4L, 5L, 6L, 7L)
        );

        // when, then
        assertThatThrownBy(() -> meetingService.save(meetingRequest, 1L))
                .isInstanceOf(InvalidParticipantException.class);
    }

    @DisplayName("미팅이 생성될 때, 참가자 명단이 비어있는 경우 예외를 반환한다.")
    @Test
    void save_throwException_ifUserIdsBlank() {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of()
        );

        // when, then
        assertThatThrownBy(() -> meetingService.save(meetingRequest, 1L))
                .isInstanceOf(InvalidParticipantException.class);
    }

    @DisplayName("미팅이 생성될 때, 참가자 명단에 존재하지 않는 user가 들어가있는 경우 예외를 반환한다.")
    @Test
    void save_throwException_ifNotExistIdInUserIds() {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(2L, 8L)
        );

        // when, then
        assertThatThrownBy(() -> meetingService.save(meetingRequest, 1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다.")
    @Test
    void findById() {
        // given
        final Long id = 1L;
        final MeetingResponse expectedMeetingResponse = new MeetingResponse(
                1L,
                "모임1",
                3,
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                null
        );

        given(currentDateTime.getValue())
                .willReturn(LocalDateTime.of(2022, 7, 14, 0, 0));

        // when
        final MeetingResponse response = meetingService.findById(id);

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("users")
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다_당일 출석부가 없는 경우 추가 후 조회한다.")
    @Test
    void findById_putAttendanceIfAbsent() {
        // given
        final Long id = 1L;
        final MeetingResponse expectedMeetingResponse = new MeetingResponse(
                1L,
                "모임1",
                4,
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                null
        );

        given(currentDateTime.getValue())
                .willReturn(LocalDateTime.now());

        // when
        final MeetingResponse response = meetingService.findById(id);

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("users")
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다(출석 마감 시간 전에는 당일 지각 스택은 반영되지 않는다.)")
    @Test
    void findById_ifNotOverClosingTime() {
        // given
        final Long id = 1L;
        final MeetingResponse expectedMeetingResponse = new MeetingResponse(
                1L,
                "모임1",
                3,
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(
                        new ParticipantResponse(1L, "aaa111@foo.com", "아스피", Status.PRESENT, 1),
                        new ParticipantResponse(2L, "bbb222@foo.com", "필즈", Status.TARDY, 2),
                        new ParticipantResponse(3L, "ccc333@foo.com", "포키", Status.PRESENT, 0),
                        new ParticipantResponse(4L, "ddd444@foo.com", "썬", Status.PRESENT, 0),
                        new ParticipantResponse(5L, "eee555@foo.com", "우디", Status.PRESENT, 0),
                        new ParticipantResponse(6L, "fff666@foo.com", "쿤", Status.PRESENT, 0),
                        new ParticipantResponse(7L, "ggg777@foo.com", "반듯", Status.PRESENT, 0)
                )
        );
        given(currentDateTime.getValue())
                .willReturn(LocalDateTime.of(2022, 7, 14, 0, 0));

        // when
        final MeetingResponse response = meetingService.findById(id);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다(출석 마감 시간이 지나면 당일 지각 스택도 반영된다.)")
    @Test
    void findById_ifOverClosingTime() {
        // given
        final Long id = 1L;
        final MeetingResponse expectedMeetingResponse = new MeetingResponse(
                1L,
                "모임1",
                3,
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                List.of(
                        new ParticipantResponse(1L, "aaa111@foo.com", "아스피", Status.PRESENT, 1),
                        new ParticipantResponse(2L, "bbb222@foo.com", "필즈", Status.TARDY, 3),
                        new ParticipantResponse(3L, "ccc333@foo.com", "포키", Status.PRESENT, 0),
                        new ParticipantResponse(4L, "ddd444@foo.com", "썬", Status.PRESENT, 0),
                        new ParticipantResponse(5L, "eee555@foo.com", "우디", Status.PRESENT, 0),
                        new ParticipantResponse(6L, "fff666@foo.com", "쿤", Status.PRESENT, 0),
                        new ParticipantResponse(7L, "ggg777@foo.com", "반듯", Status.PRESENT, 0)
                )
        );

        given(currentDateTime.getValue())
                .willReturn(LocalDateTime.of(2022, 7, 14, 10, 5));
        given(timeChecker.isExcessClosingTime(any(LocalTime.class), any(LocalTime.class)))
                .willReturn(true);

        // when
        final MeetingResponse response = meetingService.findById(id);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("유저 id로 유저가 속한 모든 모임을 조회한다.")
    @Test
    void findAllByUserId() {
        // given
        final long userId = 1L;
        final LocalTime entranceTime = LocalTime.of(10, 0);
        final Meeting meeting = new Meeting(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                entranceTime,
                LocalTime.of(18, 0));
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임2",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                entranceTime,
                LocalTime.of(18, 0),
                List.of(2L, 3L)
        );
        given(currentDateTime.getValue())
                .willReturn(LocalDateTime.of(2022, 7, 14, 10, 5));
        given(timeChecker.calculateClosingTime(entranceTime))
                .willReturn(entranceTime.plusMinutes(5));

        meetingService.save(meetingRequest, userId);

        // when
        final MyMeetingsResponse myMeetingsResponse = meetingService.findAllByUserId(userId);

        // then
        assertThat(myMeetingsResponse).usingRecursiveComparison()
                .ignoringFields("serverTime", "meetings.id")
                .isEqualTo(MyMeetingsResponse.of(
                        currentDateTime.getValue(),
                        List.of(
                                MyMeetingResponse.of(currentDateTime.getValue().toLocalTime(), timeChecker,
                                        meeting, 1),
                                MyMeetingResponse.of(currentDateTime.getValue().toLocalTime(), timeChecker,
                                        meetingRequest.toEntity(), 0)
                        ))
                );
    }

    @DisplayName("사용자들의 출석 여부를 변경한다.")
    @Test
    void updateAttendance() {
        // given
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);
        final Long meetingId = 1L;
        final Long userId = 1L;
        given(currentDateTime.getValue())
                .willReturn(LocalDateTime.of(2022, 07, 14, 0, 0));

        // when, then
        assertThatCode(() -> meetingService.updateAttendance(meetingId, userId, request))
                .doesNotThrowAnyException();
    }

    @DisplayName("사용자의 출석 여부를 변경하려고 할 때, 미팅방이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifMeetingNotFound() {
        // given
        final Long meetingId = 999L;
        final Long userId = 1L;
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        // when, then
        assertThatThrownBy(() -> meetingService.updateAttendance(meetingId, userId, request))
                .isInstanceOf(MeetingNotFoundException.class);
    }

    @DisplayName("사용자의 출석 여부를 변경하려고 할 때, 미팅 참가자가 존재하지 않는다면 예외가 발생한다.")
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
        assertThatThrownBy(() -> meetingService.updateAttendance(meetingId, userId, request))
                .isInstanceOf(ParticipantNotFoundException.class);
    }

    @DisplayName("사용자 출석 제출 시간이 마감 시간을 초과할 경우 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifDeadlineTimeExcess() {
        // given
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        given(timeChecker.isExcessClosingTime(any(LocalTime.class)))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> meetingService.updateAttendance(1L, 1L, request))
                .isInstanceOf(ClosingTimeExcessException.class);
    }
}
