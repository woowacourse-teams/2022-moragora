package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingsResponse;
import com.woowacourse.moragora.dto.ParticipantResponse;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.exception.participant.InvalidParticipantException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import com.woowacourse.moragora.support.ServerTimeManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MeetingServiceTest {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ServerTimeManager serverTimeManager;

    @DisplayName("미팅 방을 저장한다.")
    @Test
    void save() {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
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
        final Long meetingId = 1L;
        final Long loginId = 1L;
        final MeetingResponse expectedMeetingResponse = new MeetingResponse(
                1L,
                "모임1",
                3,
                false, true, false, true, null
        );

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 0, 0);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meetingId, loginId);

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("users")
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다_당일 출석부가 없는 경우 추가 후 조회한다.")
    @Test
    void findById_putAttendanceIfAbsent() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 1L;
        final MeetingResponse expectedMeetingResponse = new MeetingResponse(
                1L,
                "모임1",
                3,
                false, true, false, true, null
        );

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 0, 0);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meetingId, loginId);

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("users")
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다(출석 마감 시간 전에는 당일 지각 스택은 반영되지 않는다.)")
    @Test
    void findById_ifNotOverClosingTime() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 1L;
        final MeetingResponse expectedMeetingResponse = new MeetingResponse(
                1L,
                "모임1",
                3,
                false, true, false, true,
                List.of(
                        new ParticipantResponse(1L, "aaa111@foo.com", "아스피", Status.PRESENT, 1),
                        new ParticipantResponse(2L, "bbb222@foo.com", "필즈", Status.TARDY, 1),
                        new ParticipantResponse(3L, "ccc333@foo.com", "포키", Status.PRESENT, 0),
                        new ParticipantResponse(4L, "ddd444@foo.com", "썬", Status.PRESENT, 0),
                        new ParticipantResponse(5L, "eee555@foo.com", "우디", Status.PRESENT, 0),
                        new ParticipantResponse(6L, "fff666@foo.com", "쿤", Status.PRESENT, 0),
                        new ParticipantResponse(7L, "ggg777@foo.com", "반듯", Status.PRESENT, 0)
                )
        );

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 0, 0);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meetingId, loginId);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다(출석 마감 시간이 지나면 당일 지각 스택도 반영된다.)")
    @Test
    void findById_ifOverClosingTime() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 1L;
        final MeetingResponse expectedMeetingResponse = new MeetingResponse(
                1L,
                "모임1",
                3,
                false, true, false, true,
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

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 10, 5, 1);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meetingId, loginId);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다(당일 일정이 없으면 출석부를 초기화 하지 않고 기존 출석 데이터를 응답한다).")
    @Test
    void findById_if_hasNoEvent_and_hasUpcomingEvent() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 1L;
        final MeetingResponse expectedMeetingResponse = new MeetingResponse(
                1L,
                "모임1",
                0,
                false, true, false, true, null
        );

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 9, 0, 0);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meetingId, loginId);

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("users")
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다(당일부터의 일정이 없을 경우 기존의 출석 데이터를 응답한다).")
    @Test
    void findById_if_hasNoEvent_and_hasNoUpcomingEvent() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 1L;
        final MeetingResponse expectedMeetingResponse = new MeetingResponse(
                1L,
                "모임1",
                5,
                false, true, false, false, null
        );

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 11, 0, 0);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meetingId, loginId);

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("users")
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("Master인 id로 모임 상세 정보를 조회한다")
    @Test
    void findById_isMaster() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 1L;

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 10, 5);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meetingId, loginId);

        // then
        assertThat(response.getIsMaster()).isTrue();
    }

    @DisplayName("Master가 아닌 id로 모임 상세 정보를 조회한다")
    @Test
    void findById_NotMaster() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 2L;

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 10, 5);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meetingId, loginId);

        // then
        assertThat(response.getIsMaster()).isFalse();
    }

    @DisplayName("유저 id로 유저가 속한 모든 모임을 조회한다.")
    @Test
    void findAllByUserId() {
        // given
        final Long loginId = 1L;
        final Long meetingId = 1L;

        // when
        final MyMeetingsResponse response = meetingService.findAllByUserId(loginId);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(new MyMeetingsResponse(List.of(
                        new MyMeetingResponse(
                                1L, "모임1", false,
                                LocalTime.of(0, 0),
                                LocalTime.of(0, 0),
                                1, true, false, false)
                )));
    }
}
