package com.woowacourse.moragora.acceptance;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.FORKY;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.MASTER;
import static com.woowacourse.moragora.support.UserFixtures.NO_MASTER;
import static com.woowacourse.moragora.support.UserFixtures.SUN;
import static com.woowacourse.moragora.support.UserFixtures.createUsers;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.support.ServerTimeManager;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

class AttendanceAcceptanceTest extends AcceptanceTest {

    @MockBean
    private ServerTimeManager serverTimeManager;

    @DisplayName("오늘의 출석부를 요청하면 날짜에 해당하는 출석부와 상태코드 200을 반환한다.")
    @Test
    void showAttendance() {
        // given
        final User user1 = SUN.create();
        final User user2 = KUN.create();
        final User user3 = FORKY.create();
        final List<Long> userIds = saveUsers(List.of(user2, user3));
        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final Long meetingId = (long) saveMeeting(token, userIds, meeting);

        saveEvents(token, List.of(EVENT1.create(meeting)), meetingId);
        given(serverTimeManager.getDate())
                .willReturn(EVENT1.getDate());

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/attendances/today", token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("users.nickname", containsInAnyOrder(SUN.getNickname(), KUN.getNickname(), FORKY.getNickname()))
                .body("users.attendanceStatus", containsInAnyOrder("NONE", "NONE", "NONE"));
    }

    @DisplayName("오늘의 출석부를 요청할 때 오늘의 이벤트가 없다면 상태코드 400을 반환한다.")
    @Test
    void showAttendance_throwsException_ifEventNotExists() {
        // given
        final User user1 = SUN.create();
        final User user2 = KUN.create();
        final User user3 = FORKY.create();
        final List<Long> userIds = saveUsers(List.of(user2, user3));
        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final Long meetingId = (long) saveMeeting(token, userIds, meeting);

        saveEvents(token, List.of(EVENT1.create(meeting)), meetingId);
        given(serverTimeManager.getDate())
                .willReturn(EVENT1.getDate().minusDays(1));

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/attendances/today", token);

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("오늘의 일정이 존재하지 않아 출석부를 조회할 수 없습니다."));

    }

    @DisplayName("미팅 참가자의 출석을 업데이트하면 상태코드 204을 반환한다.")
    @Test
    void updateAttendance() {
        // given
        final UserAttendanceRequest userAttendanceRequest = new UserAttendanceRequest(true);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 1);

        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
                .willReturn(false);
        given(serverTimeManager.getDate())
                .willReturn(dateTime.toLocalDate());
        given(serverTimeManager.getDateAndTime())
                .willReturn(dateTime);

        final String token = signUpAndGetToken(MASTER.create());

        final List<Long> userIds = saveUsers(createUsers());
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        saveEvents(token, List.of(EVENT1.create(meeting)), (long) meetingId);

        // when
        final ValidatableResponse response = post(
                "/meetings/" + meetingId + "/users/" + userIds.get(0) + "/attendances/today",
                userAttendanceRequest, token);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("마스터가 아닌 참가자가 미팅 참가자의 출석을 업데이트하면 상태코드 403을 반환한다.")
    @Test
    void updateAttendance_NotMaster() {
        final UserAttendanceRequest userAttendanceRequest = new UserAttendanceRequest(true);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 1);

        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
                .willReturn(false);
        given(serverTimeManager.getDate())
                .willReturn(dateTime.toLocalDate());
        given(serverTimeManager.getDateAndTime())
                .willReturn(dateTime);

        final String masterToken = signUpAndGetToken(MASTER.create());
        final String noMasterToken = signUpAndGetToken(NO_MASTER.create());

        final Meeting meeting = MORAGORA.create();
        // TODO: signup(), login() 이용해서 NO_MASTER의 id 추출
        final int meetingId = saveMeeting(masterToken, List.of(2L), meeting);

        saveEvents(masterToken, List.of(EVENT1.create(meeting)), (long) meetingId);

        // when
        final ValidatableResponse response = post(
                "/meetings/" + meetingId + "/users/" + 1 + "/attendances/today",
                userAttendanceRequest, noMasterToken);

        // then
        response.statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", equalTo("마스터 권한이 없습니다."));
    }
//
//    @DisplayName("모임에 쌓인 커피스택을 사용하면 참가자들의 커피 스택을 차감하고 상태코드 204을 반환한다.")
//    @Test
//    void useCoffeeStack() {
//        // given
//        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 15, 0, 0);
//        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
//                .willReturn(false);
//        given(serverTimeManager.getDateAndTime())
//                .willReturn(dateTime);
//        given(serverTimeManager.getDate())
//                .willReturn(dateTime.toLocalDate());
//
//        final String token = signUpAndGetToken(MASTER.create());
//
//        final List<Long> userIds = saveUsers(createUsers());
//        final int meetingId = saveMeeting(token, userIds, MORAGORA.create());
//
//        // when
//        final ValidatableResponse response = RestAssured.given().log().all()
//                .auth().oauth2(token)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .when().post("/meetings/" + meetingId + "/coffees/use")
//                .then().log().all();
//
//        // then
//        response.statusCode(HttpStatus.NO_CONTENT.value());
//    }
//
//    @DisplayName("마스터가 아닌 유저가 커피스택 비우기를 요청시 상태코드 403울 반환한다.")
//    @Test
//    void useCoffeeStack_NotMaster() {
//        // given
//
//        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 15, 0, 0);
//        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
//                .willReturn(false);
//        given(serverTimeManager.getDateAndTime())
//                .willReturn(dateTime);
//        given(serverTimeManager.getDate())
//                .willReturn(dateTime.toLocalDate());
//
//        final String masterToken = signUpAndGetToken(MASTER.create());
//        final String noMasterToken = signUpAndGetToken(NO_MASTER.create());
//
//        final List<User> users = createUsers();
//        users.add(NO_MASTER.create());
//        final List<Long> userIds = saveUsers(users);
//        final int meetingId = saveMeeting(masterToken, userIds, MORAGORA.create());
//
//        // when
//        final ValidatableResponse response = RestAssured.given().log().all()
//                .auth().oauth2(noMasterToken)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .when().post("/meetings/" + meetingId + "/coffees/use")
//                .then().log().all();
//
//        // then
//        response.statusCode(HttpStatus.FORBIDDEN.value());
//    }
}
