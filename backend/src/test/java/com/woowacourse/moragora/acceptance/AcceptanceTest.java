package com.woowacourse.moragora.acceptance;

import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.request.event.EventRequest;
import com.woowacourse.moragora.dto.request.event.EventsRequest;
import com.woowacourse.moragora.dto.request.meeting.MeetingRequest;
import com.woowacourse.moragora.dto.request.user.UserRequest;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanUp.afterPropertiesSet();

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
        final UserRequest userRequest = new UserRequest(user.getEmail(), password, user.getNickname());
        final ValidatableResponse response = post("/users", userRequest);

        final String value = response
                .extract()
                .header("Location")
                .split("/users/")[1];

        return Long.valueOf(value);
    }

    protected String login(final User user) {
        final LoginRequest loginRequest = new LoginRequest(user.getEmail(), "1234asdf!");
        final ExtractableResponse<Response> response = post("/login", loginRequest).extract();

        return response.jsonPath().get("accessToken");
    }

    protected String signUpAndGetToken(final User user) {
        signUp(user);
        return login(user);
    }

    protected List<Long> saveUsers(final List<User> users) {
        final List<UserRequest> userRequests = users.stream()
                .map(user -> new UserRequest(user.getEmail(), "1234asdf!", user.getNickname()))
                .collect(Collectors.toList());

        final List<Long> userIds = new ArrayList<>();
        for (UserRequest userRequest : userRequests) {
            final ValidatableResponse response = post("/users", userRequest);

            final String value = response
                    .extract()
                    .header("Location")
                    .split("/users/")[1];

            final Long id = Long.valueOf(value);
            userIds.add(id);
        }

        return userIds;
    }

    protected int saveMeeting(final String token, final List<Long> userIds, final Meeting meeting) {
        final MeetingRequest meetingRequest = MeetingRequest.builder()
                .name(meeting.getName())
                .userIds(userIds)
                .build();

        final ValidatableResponse response = post("/meetings", meetingRequest, token);

        final String value = response
                .extract()
                .header("Location")
                .split("/meetings/")[1];

        return Integer.parseInt(value);
    }

    protected void saveEvents(final String token, final List<Event> events, final Long meetingId) {
        final List<EventRequest> eventRequests = events.stream()
                .map(event -> new EventRequest(event.getStartTime(), event.getEndTime(), event.getDate()))
                .collect(Collectors.toList());

        EventsRequest eventsRequest = new EventsRequest(eventRequests);
        post("/meetings/" + meetingId + "/events", eventsRequest, token);
    }
}
