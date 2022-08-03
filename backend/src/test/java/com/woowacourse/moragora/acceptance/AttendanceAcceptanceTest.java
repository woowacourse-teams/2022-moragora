package com.woowacourse.moragora.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.support.ServerTimeManager;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class AttendanceAcceptanceTest extends AcceptanceTest {

    @MockBean
    private ServerTimeManager serverTimeManager;

    @DisplayName("미팅 참가자의 출석을 업데이트하면 상태코드 204을 반환한다.")
    @Test
    void updateAttendance() {
        // given
        final int meetingId = 1;
        final int userId = 1;
        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 0, 0);
        final UserAttendanceRequest userAttendanceRequest = new UserAttendanceRequest(Status.PRESENT);
        final LoginRequest masterLoginRequest = new LoginRequest("aaa111@foo.com", "1234smart!");

        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
                .willReturn(false);
        given(serverTimeManager.getDate())
                .willReturn(dateTime.toLocalDate());

        // when
        final ValidatableResponse response = put("/meetings/" + meetingId + "/users/" + userId,
                userAttendanceRequest, signInAndGetToken(masterLoginRequest));

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("마스터가 아닌 참가자가 미팅 참가자의 출석을 업데이트하면 상태코드 403을 반환한다.")
    @Test
    void updateAttendance_NotMaster() {
        // given
        final int meetingId = 1;
        final int userId = 1;
        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 0, 0);
        final UserAttendanceRequest userAttendanceRequest = new UserAttendanceRequest(Status.PRESENT);
        final LoginRequest noMasterLoginRequest = new LoginRequest("bbb222@foo.com", "1234smart!");

        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
                .willReturn(false);
        given(serverTimeManager.getDate())
                .willReturn(dateTime.toLocalDate());

        // when
        final ValidatableResponse response = put("/meetings/" + meetingId + "/users/" + userId,
                userAttendanceRequest, signInAndGetToken(noMasterLoginRequest));

        // then
        response.statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", equalTo("마스터 권한이 없습니다."));
    }

    @DisplayName("다음에 차감될 커피스택 정보를 요청하면 사용자별 커피 개수와 상태코드 200을 반환한다.")
    @Test
    void showUserCoffeeStats() {
        // given
        final Long meetingId = 1L;

        // 마스터 로그인
        final LoginRequest masterLoginRequest = new LoginRequest("aaa111@foo.com", "1234smart!");
        final String token = signInAndGetToken(masterLoginRequest);

        // 출석 데이터 생성
        final UserAttendanceRequest userAttendanceRequest = new UserAttendanceRequest(Status.PRESENT);
        final LocalDateTime dateTime1 = LocalDateTime.of(2022, 7, 15, 0, 0);
        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
                .willReturn(false);
        given(serverTimeManager.getDateAndTime())
                .willReturn(dateTime1);
        given(serverTimeManager.getDate())
                .willReturn(dateTime1.toLocalDate());
        get("/meetings/" + meetingId, token);
        put("/meetings/" + meetingId + "/users/" + 1,
                userAttendanceRequest, signInAndGetToken(masterLoginRequest));

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/coffees/use");

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("userCoffeeStats.find{it.id == 1}.coffeeCount", equalTo(1))
                .body("userCoffeeStats.find{it.id == 2}.coffeeCount", equalTo(3))
                .body("userCoffeeStats.find{it.id == 3}.coffeeCount", equalTo(1))
                .body("userCoffeeStats.find{it.id == 4}.coffeeCount", equalTo(1))
                .body("userCoffeeStats.find{it.id == 5}.coffeeCount", equalTo(1))
        ;
    }

    @DisplayName("모임에 쌓인 커피스택을 사용하면 참가자들의 커피 스택을 차감하고 상태코드 204을 반환한다.")
    @Test
    void useCoffeeStack() {
        // given
        final int meetingId = 1;
        final LoginRequest masterLoginRequest = new LoginRequest("aaa111@foo.com", "1234smart!");
        final String token = signInAndGetToken(masterLoginRequest);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 15, 0, 0);
        given(serverTimeManager.getDateAndTime())
                .willReturn(dateTime);
        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
                .willReturn(false);
        given(serverTimeManager.getDateAndTime())
                .willReturn(dateTime);
        given(serverTimeManager.getDate())
                .willReturn(dateTime.toLocalDate());
        get("/meetings/" + meetingId, token);

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/meetings/" + meetingId + "/coffees/use")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("마스터가 아닌 유저가 커피스택 비우기를 요청시 상태코드 403울 반환한다.")
    @Test
    void useCoffeeStack_NotMaster() {
        // given
        final int meetingId = 1;
        final LoginRequest noMasterLoginRequest = new LoginRequest("bbb222@foo.com", "1234smart!");
        final String token = signInAndGetToken(noMasterLoginRequest);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 15, 0, 0);
        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
                .willReturn(false);
        given(serverTimeManager.getDateAndTime())
                .willReturn(dateTime);
        given(serverTimeManager.getDate())
                .willReturn(dateTime.toLocalDate());
        get("/meetings/" + meetingId, token);

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/meetings/" + meetingId + "/coffees/use")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.FORBIDDEN.value());
    }
}
