//package com.woowacourse.moragora.acceptance;
//
//import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
//import static com.woowacourse.moragora.support.UserFixtures.MASTER;
//import static com.woowacourse.moragora.support.UserFixtures.NO_MASTER;
//import static com.woowacourse.moragora.support.UserFixtures.createUsers;
//import static org.hamcrest.Matchers.equalTo;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//import com.woowacourse.moragora.dto.UserAttendanceRequest;
//import com.woowacourse.moragora.entity.Status;
//import com.woowacourse.moragora.entity.user.User;
//import com.woowacourse.moragora.support.ServerTimeManager;
//import io.restassured.RestAssured;
//import io.restassured.response.ValidatableResponse;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//
//class AttendanceAcceptanceTest extends AcceptanceTest {
//
//    @MockBean
//    private ServerTimeManager serverTimeManager;
//
//    @DisplayName("미팅 참가자의 출석을 업데이트하면 상태코드 204을 반환한다.")
//    @Test
//    void updateAttendance() {
//        // given
//        final UserAttendanceRequest userAttendanceRequest = new UserAttendanceRequest(Status.PRESENT);
//
//        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 1);
//
//        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
//                .willReturn(false);
//        given(serverTimeManager.getDate())
//                .willReturn(dateTime.toLocalDate());
//        given(serverTimeManager.getDateAndTime())
//                .willReturn(dateTime);
//
//        final String token = signUpAndGetToken(MASTER.create());
//
//        final List<Long> userIds = saveUsers(createUsers());
//        final int meetingId = saveMeeting(token, userIds, MORAGORA.create());
//
//        // when
//        final ValidatableResponse response = put("/meetings/" + meetingId + "/users/" + userIds.get(0),
//                userAttendanceRequest, token);
//
//        // then
//        response.statusCode(HttpStatus.NO_CONTENT.value());
//    }
//
//    @DisplayName("마스터가 아닌 참가자가 미팅 참가자의 출석을 업데이트하면 상태코드 403을 반환한다.")
//    @Test
//    void updateAttendance_NotMaster() {
//        final UserAttendanceRequest userAttendanceRequest = new UserAttendanceRequest(Status.PRESENT);
//
//        final LocalDateTime dateTime = LocalDateTime.of(2022, 8, 1, 10, 1);
//
//        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
//                .willReturn(false);
//        given(serverTimeManager.getDate())
//                .willReturn(dateTime.toLocalDate());
//        given(serverTimeManager.getDateAndTime())
//                .willReturn(dateTime);
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
//        final ValidatableResponse response = put("/meetings/" + meetingId + "/users/" + userIds.get(0),
//                userAttendanceRequest, noMasterToken);
//
//        // then
//        response.statusCode(HttpStatus.FORBIDDEN.value())
//                .body("message", equalTo("마스터 권한이 없습니다."));
//    }
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
//}
