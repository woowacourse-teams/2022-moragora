package com.woowacourse.moragora.acceptance;

import static com.woowacourse.moragora.support.MeetingFixtures.F12;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.MASTER;
import static com.woowacourse.moragora.support.UserFixtures.createUsers;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.user.User;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("모임 관련 기능")
public class MeetingAcceptanceTest extends AcceptanceTest {

    @DisplayName("사용자가 모임을 등록하고 상태코드 200 OK 를 반환받는다.")
    @Test
    void add() {
        // given
        final List<Long> userIds = saveUsers(createUsers());
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                userIds
        );

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
        final String token = signUpAndGetToken(MASTER.create());
        final List<User> users = createUsers();
        final List<String> userEmails = users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());

        final List<String> userNames = users.stream()
                .map(User::getNickname)
                .collect(Collectors.toList());

        final List<Long> userIds = saveUsers(users);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        userIds.add(1L);
        userNames.add(MASTER.getNickname());
        userEmails.add(MASTER.getEmail());

        final List<Integer> ids = userIds.stream()
                .map(Long::intValue)
                .collect(Collectors.toList());

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId, token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", equalTo(meetingId))
                .body("name", equalTo(meeting.getName()))
                .body("attendanceCount", equalTo(1))
                .body("startDate", equalTo(meeting.getStartDate().toString()))
                .body("endDate", equalTo(meeting.getEndDate().toString()))
                .body("entranceTime", equalTo(meeting.getEntranceTime().toString()))
                .body("leaveTime", equalTo(meeting.getLeaveTime().toString()))
                .body("users.id", equalTo(ids))
                .body("users.nickname", equalTo(userNames))
                .body("users.email", equalTo(userEmails));
    }

    @DisplayName("사용자가 자신이 속한 모든 모임을 조회하면 모임 정보와 상태코드 200을 반환한다.")
    @Test
    void findMy() {
        // given
        final String token = signUpAndGetToken(MASTER.create());
        final Meeting meeting1 = MORAGORA.create();
        final Meeting meeting2 = F12.create();

        final String closingTimeValue1 = meeting1.getEntranceTime().plusMinutes(5).toString();
        final String closingTimeValue2 = meeting2.getEntranceTime().plusMinutes(5).toString();

        final User user = KUN.create();
        final List<Long> ids = saveUsers(List.of(user));

        final int meetingId1 = saveMeeting(token, ids, meeting1);
        final int meetingId2 = saveMeeting(token, ids, meeting2);

        // when
        final ValidatableResponse response = get("/meetings/me", token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("meetings.id", containsInAnyOrder(meetingId1, meetingId2))
                .body("meetings.name", containsInAnyOrder(meeting1.getName(), meeting2.getName()))
                .body("meetings.startDate",
                        containsInAnyOrder(meeting1.getStartDate().toString(), meeting2.getStartDate().toString()))
                .body("meetings.endDate",
                        containsInAnyOrder(meeting1.getEndDate().toString(), meeting2.getEndDate().toString()))
                .body("meetings.entranceTime", containsInAnyOrder(meeting1.getEntranceTime().toString(),
                        meeting2.getEntranceTime().toString()))
                .body("meetings.closingTime",
                        containsInAnyOrder(closingTimeValue1, closingTimeValue2))
                .body("meetings.tardyCount", containsInAnyOrder(0, 0));
    }
}
