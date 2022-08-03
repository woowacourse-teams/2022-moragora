package com.woowacourse.moragora.service;

import static com.woowacourse.moragora.support.MeetingFixtures.F12;
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
import com.woowacourse.moragora.dto.MyMeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingsResponse;
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
                .entranceTime(meeting.getEntranceTime())
                .leaveTime(meeting.getLeaveTime())
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
                .entranceTime(meeting.getEntranceTime())
                .leaveTime(meeting.getLeaveTime())
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
                .entranceTime(meeting.getEntranceTime())
                .leaveTime(meeting.getLeaveTime())
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
                .entranceTime(meeting.getEntranceTime())
                .leaveTime(meeting.getLeaveTime())
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
                .entranceTime(meeting.getEntranceTime())
                .leaveTime(meeting.getLeaveTime())
                .userIds(List.of(99L))
                .build();

        // when, then
        assertThatThrownBy(() -> meetingService.save(meetingRequest, user.getId()))
                .isInstanceOf(UserNotFoundException.class);
    }

//    @DisplayName("id로 모임 상세 정보를 조회한다.")
//    @Test
//    void findById() {
//        // given
//        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
//        final User user = dataSupport.saveUser(KUN.create());
//        final Participant participant = dataSupport.saveParticipant(user, meeting);
//        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 1), Status.TARDY);
//        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 2), Status.TARDY);
//        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 3), Status.TARDY);
//
//        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
//                .id(meeting.getId())
//                .name(meeting.getName())
//                .attendanceCount(3)
//                .startDate(meeting.getStartDate())
//                .endDate(meeting.getEndDate())
//                .entranceTime(meeting.getEntranceTime())
//                .leaveTime(meeting.getLeaveTime())
//                .build();
//
//        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 3, 10, 6);
//        serverTimeManager.refresh(dateTime);
//
//        // when
//        final MeetingResponse response = meetingService.findById(meeting.getId());
//
//        // then
//        assertThat(response).usingRecursiveComparison()
//                .ignoringFields("users")
//                .isEqualTo(expectedMeetingResponse);
//    }
//
//    @DisplayName("id로 모임 상세 정보를 조회한다_당일 출석부가 없는 경우 추가 후 조회한다.")
//    @Test
//    void findById_putAttendanceIfAbsent() {
//        // given
//        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
//        final User user = dataSupport.saveUser(KUN.create());
//        final Participant participant = dataSupport.saveParticipant(user, meeting);
//        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 1), Status.TARDY);
//        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 2), Status.TARDY);
//        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 3), Status.TARDY);
//
//        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
//                .id(meeting.getId())
//                .name(meeting.getName())
//                .attendanceCount(4)
//                .startDate(meeting.getStartDate())
//                .endDate(meeting.getEndDate())
//                .entranceTime(meeting.getEntranceTime())
//                .leaveTime(meeting.getLeaveTime())
//                .build();
//
//        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 4, 10, 0);
//        serverTimeManager.refresh(dateTime);
//
//        // when
//        final MeetingResponse response = meetingService.findById(meeting.getId());
//
//        // then
//        assertThat(response).usingRecursiveComparison()
//                .ignoringFields("users")
//                .isEqualTo(expectedMeetingResponse);
//    }

//    @DisplayName("id로 모임 상세 정보를 조회한다(출석 마감 시간 전에는 당일 지각 스택은 반영되지 않는다.)")
//    @Test
//    void findById_ifNotOverClosingTime() {
//        // given
//        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
//        final User user1 = dataSupport.saveUser(KUN.create());
//        final Participant participant1 = dataSupport.saveParticipant(user1, meeting);
//        final Attendance attendance1 = dataSupport.saveAttendance(participant1, LocalDate.of(2022, 8, 1),
//                Status.TARDY);
//
//        final User user2 = dataSupport.saveUser(PHILLZ.create());
//        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
//        final Attendance attendance2 = dataSupport.saveAttendance(participant2, LocalDate.of(2022, 8, 1),
//                Status.TARDY);
//
//        final User user3 = dataSupport.saveUser(WOODY.create());
//        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);
//        final Attendance attendance3 = dataSupport.saveAttendance(participant3, LocalDate.of(2022, 8, 1),
//                Status.TARDY);
//
//        final List<ParticipantResponse> usersResponse = List.of(
//                new ParticipantResponse(user1.getId(), user1.getEmail(), user1.getNickname(),
//                        attendance1.getStatus(), 0),
//                new ParticipantResponse(user2.getId(), user2.getEmail(), user2.getNickname(),
//                        attendance2.getStatus(), 0),
//                new ParticipantResponse(user3.getId(), user3.getEmail(), user3.getNickname(),
//                        attendance3.getStatus(), 0)
//        );
//        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
//                .id(meeting.getId())
//                .name(meeting.getName())
//                .attendanceCount(1)
//                .startDate(meeting.getStartDate())
//                .endDate(meeting.getEndDate())
//                .entranceTime(meeting.getEntranceTime())
//                .leaveTime(meeting.getLeaveTime())
//                .usersResponse(usersResponse)
//                .build();
//
//        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 4);
//        serverTimeManager.refresh(dateTime);
//
//        // when
//        final MeetingResponse response = meetingService.findById(meeting.getId());
//
//        // then
//        assertThat(response).usingRecursiveComparison()
//                .isEqualTo(expectedMeetingResponse);
//    }
//
//    @DisplayName("id로 모임 상세 정보를 조회한다(출석 마감 시간이 지나면 당일 지각 스택도 반영된다.)")
//    @Test
//    void findById_ifOverClosingTime() {
//        // given
//        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
//        final User user1 = dataSupport.saveUser(KUN.create());
//        final Participant participant1 = dataSupport.saveParticipant(user1, meeting);
//        final Attendance attendance1 = dataSupport.saveAttendance(participant1, LocalDate.of(2022, 8, 1),
//                Status.TARDY);
//
//        final User user2 = dataSupport.saveUser(PHILLZ.create());
//        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
//        final Attendance attendance2 = dataSupport.saveAttendance(participant2, LocalDate.of(2022, 8, 1),
//                Status.TARDY);
//
//        final User user3 = dataSupport.saveUser(WOODY.create());
//        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);
//        final Attendance attendance3 = dataSupport.saveAttendance(participant3, LocalDate.of(2022, 8, 1),
//                Status.TARDY);
//
//        final List<ParticipantResponse> usersResponse = List.of(
//                new ParticipantResponse(user1.getId(), user1.getEmail(), user1.getNickname(),
//                        attendance1.getStatus(), 1),
//                new ParticipantResponse(user2.getId(), user2.getEmail(), user2.getNickname(),
//                        attendance2.getStatus(), 1),
//                new ParticipantResponse(user3.getId(), user3.getEmail(), user3.getNickname(),
//                        attendance3.getStatus(), 1)
//        );
//        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
//                .id(meeting.getId())
//                .name(meeting.getName())
//                .attendanceCount(1)
//                .startDate(meeting.getStartDate())
//                .endDate(meeting.getEndDate())
//                .entranceTime(meeting.getEntranceTime())
//                .leaveTime(meeting.getLeaveTime())
//                .usersResponse(usersResponse)
//                .build();
//
//        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 6);
//        serverTimeManager.refresh(dateTime);
//
//        // when
//        final MeetingResponse response = meetingService.findById(meeting.getId());
//
//        // then
//        assertThat(response).usingRecursiveComparison()
//                .isEqualTo(expectedMeetingResponse);
//    }

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

//    @DisplayName("유저 id로 유저가 속한 모든 모임을 조회한다.")
//    @Test
//    void findAllByUserId() {
//        // given
//        final User user = dataSupport.saveUser(KUN.create());
//        final Meeting meeting1 = dataSupport.saveMeeting(MORAGORA.create());
//        final Meeting meeting2 = dataSupport.saveMeeting(F12.create());
//
//        final Participant participant = dataSupport.saveParticipant(user, meeting1);
//        dataSupport.saveAttendance(participant, LocalDate.of(2022, 8, 1), Status.TARDY);
//
//        dataSupport.saveParticipant(user, meeting2);
//
//        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 6);
//        serverTimeManager.refresh(dateTime);
//
//        // when
//        final MyMeetingsResponse myMeetingsResponse = meetingService.findAllByUserId(user.getId());
//
//        // then
//        assertThat(myMeetingsResponse).usingRecursiveComparison()
//                .ignoringFields("serverTime", "meetings.id")
//                .isEqualTo(new MyMeetingsResponse(
//                        serverTimeManager.getDateAndTime(),
//                        List.of(
//                                MyMeetingResponse.of(meeting1, false,
//                                        serverTimeManager.calculateClosingTime(meeting1.getEntranceTime()), 1),
//                                MyMeetingResponse.of(meeting2, false,
//                                        serverTimeManager.calculateClosingTime(meeting2.getEntranceTime()), 0)
//                        ))
//                );
//    }
}
