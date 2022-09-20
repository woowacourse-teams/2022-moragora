package com.woowacourse.moragora.application;

import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT3;
import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT4;
import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.MeetingFixtures.TEATIME;
import static com.woowacourse.moragora.support.fixture.UserFixtures.KUN;
import static com.woowacourse.moragora.support.fixture.UserFixtures.MASTER;
import static com.woowacourse.moragora.support.fixture.UserFixtures.PHILLZ;
import static com.woowacourse.moragora.support.fixture.UserFixtures.SUN;
import static com.woowacourse.moragora.support.fixture.UserFixtures.WOODY;
import static com.woowacourse.moragora.support.fixture.UserFixtures.createUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.domain.attendance.Status;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.dto.request.meeting.MasterRequest;
import com.woowacourse.moragora.dto.request.meeting.MeetingRequest;
import com.woowacourse.moragora.dto.request.meeting.MeetingUpdateRequest;
import com.woowacourse.moragora.dto.response.event.EventResponse;
import com.woowacourse.moragora.dto.response.meeting.MeetingResponse;
import com.woowacourse.moragora.dto.response.meeting.MyMeetingResponse;
import com.woowacourse.moragora.dto.response.meeting.MyMeetingsResponse;
import com.woowacourse.moragora.dto.response.meeting.ParticipantResponse;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.global.InvalidFormatException;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.exception.participant.InvalidParticipantException;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
        final Participant participant = dataSupport.saveParticipant(user, meeting, true);
        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        final Event event3 = dataSupport.saveEvent(EVENT3.create(meeting));
        dataSupport.saveAttendance(participant, event1, Status.TARDY);
        dataSupport.saveAttendance(participant, event2, Status.TARDY);
        dataSupport.saveAttendance(participant, event3, Status.TARDY);

        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .participantResponses(List.of(ParticipantResponse.of(participant, 3)))
                .attendedEventCount(3)
                .isCoffeeTime(true)
                .isActive(false)
                .isLoginUserMaster(true)
                .build();

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 4, 10, 6);
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

        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        final Event event3 = dataSupport.saveEvent(EVENT3.create(meeting));
        dataSupport.saveAttendance(participant, event1, Status.TARDY);
        dataSupport.saveAttendance(participant, event2, Status.TARDY);
        dataSupport.saveAttendance(participant, event3, Status.TARDY);

        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .participantResponses(List.of(ParticipantResponse.of(participant, 3)))
                .attendedEventCount(3)
                .isCoffeeTime(true)
                .isActive(false)
                .isLoginUserMaster(false)
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
        final Participant participant1 = dataSupport.saveParticipant(user1, meeting, true);
        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveAttendance(participant1, event1, Status.NONE);

        final User user2 = dataSupport.saveUser(PHILLZ.create());
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
        dataSupport.saveAttendance(participant2, event1, Status.NONE);

        final User user3 = dataSupport.saveUser(WOODY.create());
        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);
        dataSupport.saveAttendance(participant3, event1, Status.NONE);

        final List<ParticipantResponse> usersResponse = List.of(
                new ParticipantResponse(user1.getId(), user1.getEmail(), user1.getNickname(), 0,
                        participant1.getIsMaster()),
                new ParticipantResponse(user2.getId(), user2.getEmail(), user2.getNickname(), 0,
                        participant2.getIsMaster()),
                new ParticipantResponse(user3.getId(), user3.getEmail(), user3.getNickname(), 0,
                        participant3.getIsMaster())
        );

        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .participantResponses(usersResponse)
                .attendedEventCount(1)
                .isCoffeeTime(false)
                .isActive(true)
                .isLoginUserMaster(true)
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
        final Participant participant1 = dataSupport.saveParticipant(user1, meeting, true);
        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveAttendance(participant1, event1, Status.TARDY);

        final User user2 = dataSupport.saveUser(PHILLZ.create());
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
        dataSupport.saveAttendance(participant2, event1, Status.TARDY);

        final User user3 = dataSupport.saveUser(WOODY.create());
        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);
        dataSupport.saveAttendance(participant3, event1, Status.TARDY);

        final List<ParticipantResponse> usersResponse = List.of(
                new ParticipantResponse(user1.getId(), user1.getEmail(), user1.getNickname(), 1,
                        participant1.getIsMaster()),
                new ParticipantResponse(user2.getId(), user2.getEmail(), user2.getNickname(), 1,
                        participant2.getIsMaster()),
                new ParticipantResponse(user3.getId(), user3.getEmail(), user3.getNickname(), 1,
                        participant3.getIsMaster())
        );

        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .participantResponses(usersResponse)
                .attendedEventCount(1)
                .isCoffeeTime(true)
                .isActive(false)
                .isLoginUserMaster(true)
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
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final User user = dataSupport.saveUser(KUN.create());
        final Participant participant = dataSupport.saveParticipant(user, meeting, true);
        final Event event = dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveEvent(EVENT2.create(meeting));
        dataSupport.saveAttendance(participant, event, Status.PRESENT);

        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .attendedEventCount(1)
                .isCoffeeTime(false)
                .isActive(true)
                .isLoginUserMaster(true)
                .build();

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 9, 59);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meeting.getId(), user.getId());

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("users")
                .isEqualTo(expectedMeetingResponse);
    }

    @DisplayName("id로 모임 상세 정보를 조회한다(당일부터의 일정이 없을 경우 기존의 출석 데이터를 응답한다).")
    @Test
    void findById_if_hasNoEvent_and_hasNoUpcomingEvent() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final User user = dataSupport.saveUser(KUN.create());
        final Participant participant = dataSupport.saveParticipant(user, meeting, true);
        final Event event = dataSupport.saveEvent(EVENT1.create(meeting));
        dataSupport.saveAttendance(participant, event, Status.PRESENT);

        final MeetingResponse expectedMeetingResponse = MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .attendedEventCount(1)
                .isCoffeeTime(false)
                .isActive(false)
                .isLoginUserMaster(true)
                .build();

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 2, 9, 59);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meeting.getId(), user.getId());

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

        dataSupport.saveParticipant(user, meeting, true);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 5);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meeting.getId(), user.getId());

        // then
        assertThat(response.getIsLoginUserMaster()).isTrue();
    }

    @DisplayName("Master가 아닌 id로 모임 상세 정보를 조회한다")
    @Test
    void findById_NotMaster() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = KUN.create();

        dataSupport.saveParticipant(user, meeting, false);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 10, 5);
        serverTimeManager.refresh(dateTime);

        // when
        final MeetingResponse response = meetingService.findById(meeting.getId(), user.getId());

        // then
        assertThat(response.getIsLoginUserMaster()).isFalse();
    }

    @DisplayName("유저 id로 유저가 속한 모든 모임을 조회한다.")
    @Test
    void findAllByUserId() {
        // given
        final Meeting meeting1 = MORAGORA.create();
        final Meeting meeting2 = TEATIME.create();
        final User user = KUN.create();
        dataSupport.saveParticipant(user, meeting1, true);
        dataSupport.saveParticipant(user, meeting2, true);
        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting1));
        final Event event2 = dataSupport.saveEvent(EVENT1.create(meeting1));

        final LocalTime entranceTime = event1.getStartTime();
        final EventResponse upcomingEvent = EventResponse.of(event1, entranceTime.minusMinutes(30),
                entranceTime.plusMinutes(5));

        final MyMeetingResponse response1 = MyMeetingResponse.builder()
                .id(meeting1.getId())
                .name(meeting1.getName())
                .tardyCount(0)
                .isLoginUserMaster(true)
                .isCoffeeTime(false)
                .isActive(false)
                .upcomingEvent(upcomingEvent)
                .build();

        final MyMeetingResponse response2 = MyMeetingResponse.builder()
                .id(meeting2.getId())
                .name(meeting2.getName())
                .tardyCount(0)
                .isLoginUserMaster(true)
                .isCoffeeTime(false)
                .isActive(false)
                .upcomingEvent(null)
                .build();

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 5);
        serverTimeManager.refresh(dateTime);

        // when
        final MyMeetingsResponse response = meetingService.findAllByUserId(user.getId());

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(new MyMeetingsResponse(List.of(response1, response2)));
    }

    @DisplayName("미팅의 마스터를 다른 참가자로 수정한다.")
    @Test
    void updateMaster() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User master = MASTER.create();
        final User participant = KUN.create();
        dataSupport.saveParticipant(master, meeting, true);
        dataSupport.saveParticipant(participant, meeting, false);

        final MasterRequest masterRequest = new MasterRequest(participant.getId());

        // when, then
        assertThatNoException()
                .isThrownBy(() -> meetingService.assignMaster(meeting.getId(), masterRequest, master.getId()));
    }

    @DisplayName("미팅의 마스터를 존재하지 않는 회원으로 수정하면 예외가 발생한다.")
    @Test
    void updateMaster_ifUserNotFound() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User master = MASTER.create();
        dataSupport.saveParticipant(master, meeting, true);

        final MasterRequest masterRequest = new MasterRequest(100L);

        // when, then
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> meetingService.assignMaster(meeting.getId(), masterRequest, master.getId()));
    }

    @DisplayName("존재하지 않는 미팅의 마스터를 수정하면 예외가 발생한다.")
    @Test
    void updateMaster_ifMeetingNotFound() {
        // given
        final User master = MASTER.create();
        final User participant = KUN.create();

        final MasterRequest masterRequest = new MasterRequest(participant.getId());

        // when, then
        assertThatExceptionOfType(MeetingNotFoundException.class)
                .isThrownBy(() -> meetingService.assignMaster(100L, masterRequest, master.getId()));
    }

    @DisplayName("미팅의 마스터를 참가하지 않는 회원으로 수정하면 예외가 발생한다.")
    @Test
    void updateMaster_ifNotParticipant() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User master = MASTER.create();
        final User user = dataSupport.saveUser(KUN.create());
        dataSupport.saveParticipant(master, meeting, true);

        final MasterRequest masterRequest = new MasterRequest(user.getId());

        // when, then
        assertThatExceptionOfType(ParticipantNotFoundException.class)
                .isThrownBy(() -> meetingService.assignMaster(meeting.getId(), masterRequest, master.getId()));
    }

    @DisplayName("미팅의 마스터를 자신으로 수정하면 예외가 발생한다.")
    @Test
    void updateMaster_ifMe() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User master = MASTER.create();
        dataSupport.saveParticipant(master, meeting, true);

        final MasterRequest masterRequest = new MasterRequest(master.getId());

        // when, then
        assertThatExceptionOfType(ClientRuntimeException.class)
                .isThrownBy(() -> meetingService.assignMaster(meeting.getId(), masterRequest, master.getId()))
                .withMessage("스스로에게 마스터 권한을 넘길 수 없습니다.");
    }

    @DisplayName("미팅 이름을 변경한다.")
    @Test
    void updateName() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final MeetingUpdateRequest request = new MeetingUpdateRequest("체크메이트");

        // when, then
        assertThatCode(() -> meetingService.updateName(request, meeting.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 미팅의 이름을 변경하면 예외가 발생한다.")
    @Test
    void updateName_ifNotFound() {
        // given
        final MeetingUpdateRequest request = new MeetingUpdateRequest("체크메이트");

        // when, then
        assertThatThrownBy(() -> meetingService.updateName(request, 0L))
                .isInstanceOf(MeetingNotFoundException.class);
    }

    @DisplayName("변경하려는 미팅 이름이 50자를 초과하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"012345678901234567890123456789012345678901234567891",
            "영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영",
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija"})
    void updateName_ifTooLong(final String name) {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final MeetingUpdateRequest request = new MeetingUpdateRequest(name);

        // when, then
        assertThatThrownBy(() -> meetingService.updateName(request, meeting.getId()))
                .isInstanceOf(InvalidFormatException.class);
    }

    @DisplayName("모임의 참가자를 삭제한다.")
    @Test
    void deleteParticipant() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = KUN.create();
        dataSupport.saveParticipant(user, meeting, false);

        // when, then
        assertThatCode(() -> meetingService.deleteParticipant(meeting.getId(), user.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 모임의 참가자를 삭제하면 예외가 발생한다.")
    @Test
    void deleteParticipant_ifMeetingNotFound() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = KUN.create();
        dataSupport.saveParticipant(user, meeting, false);

        // when, then
        assertThatThrownBy(() -> meetingService.deleteParticipant(100L, user.getId()))
                .isInstanceOf(MeetingNotFoundException.class);
    }

    @DisplayName("존재하지 않는 유저를 모임에서 삭제하면 예외가 발생한다.")
    @Test
    void deleteParticipant_ifUserNotFound() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        // when, then
        assertThatThrownBy(() -> meetingService.deleteParticipant(meeting.getId(), 100L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("모임에 참가하지 않는 유저를 삭제하면 예외가 발생한다.")
    @Test
    void deleteParticipant_ifParticipantNotFound() {
        // given
        final Meeting meeting = MORAGORA.create();
        dataSupport.saveParticipant(KUN.create(), meeting, false);

        final User user = dataSupport.saveUser(SUN.create());

        // when, then
        assertThatThrownBy(() -> meetingService.deleteParticipant(meeting.getId(), user.getId()))
                .isInstanceOf(ParticipantNotFoundException.class);
    }

    @DisplayName("모임의 마스터인 참가자를 삭제하면 예외가 발생한다.")
    @Test
    void deleteParticipant_ifMaster() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user = KUN.create();
        dataSupport.saveParticipant(user, meeting, true);

        // when, then
        assertThatThrownBy(() -> meetingService.deleteParticipant(meeting.getId(), user.getId()))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("마스터는 모임을 나갈 수 없습니다.");
    }

    @DisplayName("미팅 삭제를 완료한다.")
    @Test
    void deleteMeeting() {
        // given
        final User user = dataSupport.saveUser(KUN.create());
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());
        final Participant participant = dataSupport.saveParticipant(user, meeting, true);
        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        final Event event3 = dataSupport.saveEvent(EVENT3.create(meeting));
        final Event event4 = dataSupport.saveEvent(EVENT4.create(meeting));

        dataSupport.saveAttendance(participant, event1, Status.NONE);
        dataSupport.saveAttendance(participant, event2, Status.NONE);
        dataSupport.saveAttendance(participant, event3, Status.NONE);
        dataSupport.saveAttendance(participant, event4, Status.NONE);

        // when
        meetingService.deleteMeeting(meeting.getId());

        // then
        assertThatThrownBy(() -> meetingService.findById(meeting.getId(), user.getId()))
                .isInstanceOf(MeetingNotFoundException.class);
    }

    @DisplayName("존재하지 않는 미팅을 삭제하려고 하면 예외가 발생한다.")
    @Test
    void deleteMeeting_throwsException_ifNotExistMeeting() {
        // given
        final User user = dataSupport.saveUser(KUN.create());

        // when, then
        assertThatThrownBy(() -> meetingService.findById(99L, user.getId()))
                .isInstanceOf(MeetingNotFoundException.class);
    }
}
