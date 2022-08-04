package com.woowacourse.moragora.service;

import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.MASTER;
import static com.woowacourse.moragora.support.UserFixtures.PHILLZ;
import static com.woowacourse.moragora.support.UserFixtures.WOODY;
import static com.woowacourse.moragora.support.UserFixtures.createUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.ParticipantResponse;
import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.participant.InvalidParticipantException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import com.woowacourse.moragora.support.ServerTimeManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private DataSupport dataSupport;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.execute();
    }

    @DisplayName("미팅 방을 저장한다.")
    @Test
    void save() {
        // given
        final User master = dataSupport.saveUser(MASTER.create());
        final List<Long> userIds = dataSupport.saveUsers(createUsers());
        final Meeting meeting = MORAGORA.create();

        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .userIds(userIds)
                .build();

        // when
        final Long expected = meetingService.save(meetingRequest, master.getId());

        // then
        assertThat(expected).isNotNull();
    }

    @DisplayName("미팅이 생성될 때, 참가자 명단에 미팅 생성자가 있는 경우 예외를 반환한다.")
    @Test
    void save_throwException_ifUserIdsContainLoginId() {
        // given
        final User master = dataSupport.saveUser(MASTER.create());
        final List<Long> userIds = dataSupport.saveUsers(createUsers());
        userIds.add(master.getId());

        final Meeting meeting = MORAGORA.create();

        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .userIds(userIds)
                .build();

        // when, then
        assertThatThrownBy(() -> meetingService.save(meetingRequest, master.getId()))
                .isInstanceOf(InvalidParticipantException.class);
    }

    @DisplayName("미팅이 생성될 때, 참가자 명단에 중복이 있는 경우 예외를 반환한다.")
    @Test
    void save_throwException_ifUserIdsDuplicated() {
        // given
        final User master = dataSupport.saveUser(MASTER.create());
        final User user = dataSupport.saveUser(KUN.create());

        final Meeting meeting = MORAGORA.create();
        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .userIds(List.of(user.getId(), user.getId()))
                .build();

        // when, then
        assertThatThrownBy(() -> meetingService.save(meetingRequest, master.getId()))
                .isInstanceOf(InvalidParticipantException.class);
    }

    @DisplayName("미팅이 생성될 때, 참가자 명단이 비어있는 경우 예외를 반환한다.")
    @Test
    void save_throwException_ifUserIdsBlank() {
        // given
        final User user = dataSupport.saveUser(MASTER.create());

        final Meeting meeting = MORAGORA.create();
        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .userIds(List.of(user.getId()))
                .build();

        // when, then
        assertThatThrownBy(() -> meetingService.save(meetingRequest, user.getId()))
                .isInstanceOf(InvalidParticipantException.class);
    }

    @DisplayName("미팅이 생성될 때, 참가자 명단에 존재하지 않는 user가 들어가있는 경우 예외를 반환한다.")
    @Test
    void save_throwException_ifNotExistIdInUserIds() {
        // given
        final User user = dataSupport.saveUser(MASTER.create());

        final Meeting meeting = MORAGORA.create();

        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .userIds(List.of(99L))
                .build();

        // when, then
        assertThatThrownBy(() -> meetingService.save(meetingRequest, user.getId()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다.")
    @Test
    void findById() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final User user = dataSupport.saveUser(KUN.create());
        final Participant participant = dataSupport.saveParticipant(user, meeting, false);
        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 1), Status.TARDY);
        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 2), Status.TARDY);
        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 3), Status.TARDY);

        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .attendanceCount(3)
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .isCoffeeTime(true)
                .build();

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 3, 10, 6);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meeting.getId(), user.getId());

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("users")
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다_당일 출석부가 없는 경우 추가 후 조회한다.")
    @Test
    void findById_putAttendanceIfAbsent() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final User user = dataSupport.saveUser(KUN.create());
        final Participant participant = dataSupport.saveParticipant(user, meeting);
        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 1), Status.TARDY);
        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 2), Status.TARDY);
        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 3), Status.TARDY);

        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .attendanceCount(4)
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .isCoffeeTime(true)
                .build();

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 4, 10, 0);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meeting.getId(), user.getId());

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("users")
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다(출석 마감 시간 전에는 당일 지각 스택은 반영되지 않는다.)")
    @Test
    void findById_ifNotOverClosingTime() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final User user1 = dataSupport.saveUser(KUN.create());
        final Participant participant1 = dataSupport.saveParticipant(user1, meeting);
        final Attendance attendance1 = dataSupport.saveAttendance(participant1, LocalDate.of(2022, 8, 1),
                Status.TARDY);

        final User user2 = dataSupport.saveUser(PHILLZ.create());
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
        final Attendance attendance2 = dataSupport.saveAttendance(participant2, LocalDate.of(2022, 8, 1),
                Status.TARDY);

        final User user3 = dataSupport.saveUser(WOODY.create());
        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);
        final Attendance attendance3 = dataSupport.saveAttendance(participant3, LocalDate.of(2022, 8, 1),
                Status.TARDY);

        final List<ParticipantResponse> usersResponse = List.of(
                new ParticipantResponse(user1.getId(), user1.getEmail(), user1.getNickname(),
                        attendance1.getStatus(), 0),
                new ParticipantResponse(user2.getId(), user2.getEmail(), user2.getNickname(),
                        attendance2.getStatus(), 0),
                new ParticipantResponse(user3.getId(), user3.getEmail(), user3.getNickname(),
                        attendance3.getStatus(), 0)
        );
        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .attendanceCount(1)
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .usersResponse(usersResponse)
                .isCoffeeTime(true)
                .build();

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 4);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meeting.getId(), user1.getId());

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다(출석 마감 시간이 지나면 당일 지각 스택도 반영된다.)")
    @Test
    void findById_ifOverClosingTime() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final User user1 = dataSupport.saveUser(KUN.create());
        final Participant participant1 = dataSupport.saveParticipant(user1, meeting);
        final Attendance attendance1 = dataSupport.saveAttendance(participant1, LocalDate.of(2022, 8, 1),
                Status.TARDY);

        final User user2 = dataSupport.saveUser(PHILLZ.create());
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
        final Attendance attendance2 = dataSupport.saveAttendance(participant2, LocalDate.of(2022, 8, 1),
                Status.TARDY);

        final User user3 = dataSupport.saveUser(WOODY.create());
        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);
        final Attendance attendance3 = dataSupport.saveAttendance(participant3, LocalDate.of(2022, 8, 1),
                Status.TARDY);

        final List<ParticipantResponse> usersResponse = List.of(
                new ParticipantResponse(user1.getId(), user1.getEmail(), user1.getNickname(),
                        attendance1.getStatus(), 1),
                new ParticipantResponse(user2.getId(), user2.getEmail(), user2.getNickname(),
                        attendance2.getStatus(), 1),
                new ParticipantResponse(user3.getId(), user3.getEmail(), user3.getNickname(),
                        attendance3.getStatus(), 1)
        );
        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .attendanceCount(1)
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .usersResponse(usersResponse)
                .isCoffeeTime(true)
                .build();

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 6);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meeting.getId(), user1.getId());

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
        final Meeting meeting = MORAGORA.create();
        final User user = KUN.create();

        final Participant participant = dataSupport.saveParticipant(user, meeting, true);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 5);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meeting.getId(), user.getId());

        // then
        assertThat(response.getIsMaster()).isTrue();
    }

    @DisplayName("Master가 아닌 id로 모임 상세 정보를 조회한다")
    @Test
    void findById_NotMaster() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = KUN.create();

        final Participant participant = dataSupport.saveParticipant(user, meeting, false);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 10, 5);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meeting.getId(), user.getId());

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
//    @DisplayName("유저 id로 유저가 속한 모든 모임을 조회한다.")
//    @Test
//    void findAllByUserId() {
//        // given
//        final User user = dataSupport.saveUser(KUN.create());
//        final Meeting meeting1 = dataSupport.saveMeeting(MORAGORA.create());
//        final Meeting meeting2 = dataSupport.saveMeeting(F12.create());
//
//        final Participant participant1 = dataSupport.saveParticipant(user, meeting1);
//        dataSupport.saveAttendance(participant1, LocalDate.of(2022, 8, 1), Status.TARDY);
//
//        final Participant participant2 = dataSupport.saveParticipant(user, meeting2);
//
//        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 6);
//        serverTimeManager.refresh(dateTime);
//        new Event(dateTime.toLocalDate(), )
//
//        // when
//        final MyMeetingsResponse myMeetingsResponse = meetingService.findAllByUserId(user.getId());
//
//        // then
//        assertThat(myMeetingsResponse).usingRecursiveComparison()
//                .isEqualTo(new MyMeetingsResponse(
//                        List.of(
//                                MyMeetingResponse.of(meeting1, false,
//                                        serverTimeManager.calculateClosingTime(LocalTime.of(10, 0)),
//                                        1, participant1.getIsMaster(), true),
//                                MyMeetingResponse.of(meeting2, false,
//                                        serverTimeManager.calculateClosingTime(meeting2.getEntranceTime()), 0,
//                                        participant2.getIsMaster(), false)
//                        ))
//                );
//    }
}
