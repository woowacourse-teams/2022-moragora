package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.exception.IllegalParticipantException;
import java.time.LocalDate;
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

    @DisplayName("미팅이 생성될 때, 참가자 명단에 미팅 생성자가 있는 경우 예외를 반환한다,")
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
                .isInstanceOf(IllegalParticipantException.class);
    }

    @DisplayName("미팅이 생성될 때, 참가자 명단에 중복이 있는 경우 예외를 반환한다,")
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
                .isInstanceOf(IllegalParticipantException.class);
    }

    @DisplayName("미팅이 생성될 때, 참가자 명단이 비어있는 경우 예외를 반환한다,")
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
                .isInstanceOf(IllegalParticipantException.class);
    }

    @DisplayName("미팅이 생성될 때, 참가자 명단에 존재하지 않는 user가 들어가있는 경우 예외를 반환한다,")
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
                .isInstanceOf(IllegalParticipantException.class);
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
}
