package com.woowacourse.moragora.acceptance;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.MeetingFixtures.F12;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.MASTER;
import static com.woowacourse.moragora.support.UserFixtures.createUsers;
import static com.woowacourse.moragora.support.UserFixtures.getEmailsIncludingMaster;
import static com.woowacourse.moragora.support.UserFixtures.getNicknamesIncludingMaster;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.support.ServerTimeManager;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@DisplayName("모임 관련 기능")
public class MeetingAcceptanceTest extends AcceptanceTest {

    @MockBean
    private ServerTimeManager serverTimeManager;

    @DisplayName("사용자가 모임을 등록하고 상태코드 200 OK 를 반환받는다.")
    @Test
    void add() {
        // given
        final List<Long> userIds = saveUsers(createUsers());

        final Meeting meeting = MORAGORA.create();
        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .userIds(userIds)
                .build();

        // when
        final ValidatableResponse response = post("/meetings", meetingRequest, signUpAndGetToken(MASTER.create()));

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", notNullValue());
    }

    @DisplayName("사용자가 특정 모임을 조회하면 해당 모임 상세 정보와 상태코드 200을 반환한다.")
    @Test
    void findOne() {
        // given
        final User loginUser = MASTER.create();
        final Long loginId = signUp(loginUser);
        final String token = login(loginUser);

        final List<User> users = createUsers();
        final List<Long> userIds = saveUsers(users);

        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        userIds.add(loginId);

        final List<Integer> ids = userIds.stream()
                .map(Long::intValue)
                .collect(Collectors.toList());

        given(serverTimeManager.getDate())
                .willReturn(LocalDate.of(2022, 8, 1));

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId, token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", equalTo(meetingId))
                .body("name", equalTo(meeting.getName()))
                .body("attendedEventCount", equalTo(0))
                .body("isLoginUserMaster", equalTo(true))
                .body("isCoffeeTime", equalTo(false))
                .body("isActive", equalTo(false))
                .body("users.id", equalTo(ids))
                .body("users.nickname", equalTo(getNicknamesIncludingMaster()))
                .body("users.email", equalTo(getEmailsIncludingMaster()));
    }

    @DisplayName("사용자가 자신이 속한 모든 모임을 조회하면 모임 정보와 상태코드 200을 반환한다.")
    @Test
    void findMy() {
        // given
        final String token = signUpAndGetToken(MASTER.create());
        final Meeting meeting1 = MORAGORA.create();
        final Meeting meeting2 = F12.create();

        final User user = KUN.create();
        final List<Long> ids = saveUsers(List.of(user));

        final int meetingId1 = saveMeeting(token, ids, meeting1);
        final int meetingId2 = saveMeeting(token, ids, meeting2);

        final Event event = EVENT1.create(meeting1);

        given(serverTimeManager.getDate())
                .willReturn(event.getDate());
        given(serverTimeManager.isAttendanceOpen(LocalTime.of(10, 0)))
                .willReturn(true);
        given(serverTimeManager.isAttendanceClosed(any(LocalTime.class)))
                .willReturn(false);
        given(serverTimeManager.calculateOpenTime(event.getStartTime()))
                .willReturn(LocalTime.of(9, 30));
        given(serverTimeManager.calculateAttendanceCloseTime(event.getStartTime()))
                .willReturn(LocalTime.of(10, 5));

        saveEvents(token, List.of(event), (long) meetingId1);

        // when
        final ValidatableResponse response = get("/meetings/me", token);

        // then
        await().untilAsserted(() -> response.statusCode(HttpStatus.OK.value())
                .body("meetings.id", containsInAnyOrder(meetingId1, meetingId2))
                .body("meetings.name", containsInAnyOrder(meeting1.getName(), meeting2.getName()))
                .body("meetings.tardyCount", containsInAnyOrder(1, 0))
                .body("meetings.isLoginUserMaster", containsInAnyOrder(true, true))
                .body("meetings.isCoffeeTime", containsInAnyOrder(true, false))
                .body("meetings.isActive", containsInAnyOrder(true, false))
                .body("meetings.find{it.id == " + meetingId1 + "}.upcomingEvent.id", equalTo(1))
                .body("meetings.find{it.id == " + meetingId1 + "}.upcomingEvent.attendanceOpenTime", equalTo("09:30"))
                .body("meetings.find{it.id == " + meetingId1 + "}.upcomingEvent.attendanceClosedTime", equalTo("10:05"))
                .body("meetings.find{it.id == " + meetingId1 + "}.upcomingEvent.meetingStartTime", equalTo("10:00"))
                .body("meetings.find{it.id == " + meetingId1 + "}.upcomingEvent.meetingEndTime", equalTo("18:00"))
                .body("meetings.find{it.id == " + meetingId1 + "}.upcomingEvent.date", equalTo("2022-08-01"))
                .body("meetings.find{it.id == " + meetingId2 + "}.upcomingEvent", equalTo(null))
        );
    }
}
