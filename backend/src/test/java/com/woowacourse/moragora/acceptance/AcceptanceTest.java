package com.woowacourse.moragora.acceptance;

import static org.mockito.BDDMockito.given;

import com.woowacourse.moragora.application.AttendanceScheduler;
import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.dto.request.auth.EmailRequest;
import com.woowacourse.moragora.dto.request.auth.EmailVerifyRequest;
import com.woowacourse.moragora.dto.request.event.EventRequest;
import com.woowacourse.moragora.dto.request.event.EventsRequest;
import com.woowacourse.moragora.dto.request.meeting.BeaconRequest;
import com.woowacourse.moragora.dto.request.meeting.BeaconsRequest;
import com.woowacourse.moragora.dto.request.meeting.MeetingRequest;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.request.user.UserRequest;
import com.woowacourse.moragora.infrastructure.GoogleClient;
import com.woowacourse.moragora.support.AsyncMailSender;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import com.woowacourse.moragora.support.RandomCodeGenerator;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

@SpringBootTest(properties = "spring.session.store-type=none", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @MockBean
    protected ServerTimeManager serverTimeManager;

    @MockBean
    protected GoogleClient googleClient;

    @MockBean
    protected AsyncMailSender mailSender;

    @MockBean
    protected RandomCodeGenerator randomCodeGenerator;

    @Autowired
    protected AttendanceScheduler attendanceScheduler;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();
    }

    protected ValidatableResponse post(final String uri, final Object requestBody) {
        return RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post(uri)
                .then().log().all();
    }

    protected ValidatableResponse post(final String uri, final Object requestBody, final String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post(uri)
                .then().log().all();
    }

    protected ValidatableResponse postWithSession(final String uri, final Object requestBody, final String sessionId) {
        return RestAssured.given().log().all()
                .sessionId(sessionId)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post(uri)
                .then().log().all();
    }

    protected ValidatableResponse get(final String uri) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all();
    }

    protected ValidatableResponse get(final String uri, final String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all();
    }

    protected ValidatableResponse put(final String uri, final Object requestBody, final String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put(uri)
                .then().log().all();
    }

    protected ValidatableResponse delete(final String uri, final String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(uri)
                .then().log().all();
    }

    protected ValidatableResponse delete(final String uri, final Object requestBody, final String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(uri)
                .then().log().all();
    }

    protected Long signUp(final User user) {
        final String password = "1234asdf!";
        final String sessionId = verifyEmailAndGetSessionId(user.getEmail());
        final UserRequest userRequest = new UserRequest(user.getEmail(), password, user.getNickname());
        final ValidatableResponse response = postWithSession("/users", userRequest, sessionId);

        final String value = response
                .extract()
                .header("Location")
                .split("/users/")[1];

        return Long.valueOf(value);
    }

    protected String login(final User user) {
        given(serverTimeManager.getDateAndTime())
                .willReturn(LocalDateTime.now());
        final LoginRequest loginRequest = new LoginRequest(user.getEmail(), "1234asdf!");
        final ExtractableResponse<Response> response = post("/login", loginRequest).extract();

        return response.jsonPath().get("accessToken");
    }

    protected String signUpAndGetToken(final User user) {
        signUp(user);
        return login(user);
    }

    protected List<Long> saveUsers(final List<User> users) {
        return users.stream()
                .map(this::signUp)
                .collect(Collectors.toList());
    }

    protected int saveMeeting(final String token, final List<Long> userIds, final Meeting meeting) {
        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .userIds(userIds)
                .build();

        final ValidatableResponse response = post("/meetings", meetingRequest, token);

        final String meetingId = response
                .extract()
                .header("Location")
                .split("/meetings/")[1];

        return Integer.parseInt(meetingId);
    }

    protected void saveEvents(final String token, final List<Event> events, final Long meetingId) {
        final List<EventRequest> eventRequests = events.stream()
                .map(event -> new EventRequest(event.getStartTime(), event.getEndTime(), event.getDate()))
                .collect(Collectors.toList());

        EventsRequest eventsRequest = new EventsRequest(eventRequests);
        post("/meetings/" + meetingId + "/events", eventsRequest, token);
    }

    protected void saveBeacons(final String token, final Long meetingId) {
        final BeaconRequest beaconRequest =
                new BeaconRequest("서울시 송파구", 37.33, 126.58, 50);
        final BeaconsRequest beaconsRequest = new BeaconsRequest(List.of(beaconRequest));

        // when
        final String uri = String.format("/meetings/%d/beacons", meetingId);
        post(uri, beaconsRequest, token);
    }

    protected String saveVerificationAndGetSessionId(final String email, final String code) {
        final EmailRequest request = new EmailRequest(email);
        given(randomCodeGenerator.generateAuthCode())
                .willReturn(code);

        final ValidatableResponse validatableResponse = post("/email/send", request);
        return validatableResponse.extract().sessionId();
    }

    protected String verifyEmailAndGetSessionId(final String email) {
        final String code = "000000";
        final LocalDateTime dateTime = LocalDateTime.of(2022, 10, 10, 10, 10);
        given(serverTimeManager.getDateAndTime())
                .willReturn(dateTime);

        final String sessionId = saveVerificationAndGetSessionId(email, code);
        final EmailVerifyRequest request = new EmailVerifyRequest(email, code);
        postWithSession("/email/verify", request, sessionId);

        return sessionId;
    }
}
