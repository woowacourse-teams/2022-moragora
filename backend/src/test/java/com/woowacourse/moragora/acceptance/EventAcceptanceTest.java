package com.woowacourse.moragora.acceptance;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.EventFixtures.EVENT3;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.AZPI;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static org.hamcrest.Matchers.containsInAnyOrder;

import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.user.User;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class EventAcceptanceTest extends AcceptanceTest {

    @DisplayName("특정 모임에 대한 일정들을 등록하고 상태코드 200을 반환한다.")
    @Test
    void add() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);

        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getEntranceTime(),
                                event1.getLeaveTime(),
                                event1.getDate()
                        ),
                        new EventRequest(
                                event2.getEntranceTime(),
                                event2.getLeaveTime(),
                                event2.getDate()
                        )
                ));

        // when
        final ValidatableResponse response = post("/meetings/" + meetingId + "/events", eventsRequest, token);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("모든 이벤트를 조회한다.")
    @Test
    void inquireByDuration_all() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);

        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getEntranceTime(),
                                event1.getLeaveTime(),
                                event1.getDate()
                        ),
                        new EventRequest(
                                event2.getEntranceTime(),
                                event2.getLeaveTime(),
                                event2.getDate()
                        )
                ));
        post("/meetings/" + meetingId + "/events", eventsRequest, token);

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/events", token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("events.attendanceOpenTime",
                        containsInAnyOrder("09:30", "09:30"))
                .body("events.attendanceClosedTime",
                        containsInAnyOrder("10:05", "10:05"))
                .body("events.meetingStartTime",
                        containsInAnyOrder("10:00", "10:00"))
                .body("events.meetingEndTime",
                        containsInAnyOrder("18:00", "18:00"))
                .body("events.date", containsInAnyOrder("2022-08-01", "2022-08-02"));
    }

    @DisplayName("특정 날짜 이후의 일정을 조회한다.")
    @Test
    void inquireByDuration_isGreaterThanEqualBegin() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        final Event event3 = EVENT3.create(meeting);

        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getEntranceTime(),
                                event1.getLeaveTime(),
                                event1.getDate()
                        ),
                        new EventRequest(
                                event2.getEntranceTime(),
                                event2.getLeaveTime(),
                                event2.getDate()
                        ),
                        new EventRequest(
                                event3.getEntranceTime(),
                                event3.getLeaveTime(),
                                event3.getDate()
                        )
                ));
        post("/meetings/" + meetingId + "/events", eventsRequest, token);

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/events?begin=" + event2.getDate(), token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("events.attendanceOpenTime",
                        containsInAnyOrder("09:30", "09:30"))
                .body("events.attendanceClosedTime",
                        containsInAnyOrder("10:05", "10:05"))
                .body("events.meetingStartTime",
                        containsInAnyOrder("10:00", "10:00"))
                .body("events.meetingEndTime",
                        containsInAnyOrder("18:00", "18:00"))
                .body("events.date", containsInAnyOrder("2022-08-02", "2022-08-03"));
    }

    @DisplayName("특정 날짜 이전의 일정을 조회한다.")
    @Test
    void inquireByDuration_isLessThanEqualEnd() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        final Event event3 = EVENT3.create(meeting);

        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getEntranceTime(),
                                event1.getLeaveTime(),
                                event1.getDate()
                        ),
                        new EventRequest(
                                event2.getEntranceTime(),
                                event2.getLeaveTime(),
                                event2.getDate()
                        ),
                        new EventRequest(
                                event3.getEntranceTime(),
                                event3.getLeaveTime(),
                                event3.getDate()
                        )
                ));
        post("/meetings/" + meetingId + "/events", eventsRequest, token);

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/events?end=" + event2.getDate(), token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("events.attendanceOpenTime",
                        containsInAnyOrder("09:30", "09:30"))
                .body("events.attendanceClosedTime",
                        containsInAnyOrder("10:05", "10:05"))
                .body("events.meetingStartTime",
                        containsInAnyOrder("10:00", "10:00"))
                .body("events.meetingEndTime",
                        containsInAnyOrder("18:00", "18:00"))
                .body("events.date", containsInAnyOrder("2022-08-01", "2022-08-02"));
    }

    @DisplayName("특정 기간의 일정을 조회한다.")
    @Test
    void find_inDuration() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        final Event event3 = EVENT3.create(meeting);

        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getEntranceTime(),
                                event1.getLeaveTime(),
                                event1.getDate()
                        ),
                        new EventRequest(
                                event2.getEntranceTime(),
                                event2.getLeaveTime(),
                                event2.getDate()
                        ),
                        new EventRequest(
                                event3.getEntranceTime(),
                                event3.getLeaveTime(),
                                event3.getDate()
                        )
                ));
        post("/meetings/" + meetingId + "/events", eventsRequest, token);

        // when
        final ValidatableResponse response = get(
                "/meetings/" + meetingId + "/events?begin=" + event2.getDate() + "&end=" + event2.getDate(), token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("events.attendanceOpenTime",
                        containsInAnyOrder("09:30"))
                .body("events.attendanceClosedTime",
                        containsInAnyOrder("10:05"))
                .body("events.meetingStartTime",
                        containsInAnyOrder("10:00"))
                .body("events.meetingEndTime",
                        containsInAnyOrder("18:00"))
                .body("events.date", containsInAnyOrder("2022-08-02"));
    }
}
