package com.woowacourse.moragora.acceptance;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.AZPI;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.support.ServerTimeManager;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

class EventAcceptanceTest extends AcceptanceTest {

    @MockBean
    private ServerTimeManager serverTimeManager;

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

    @DisplayName("모임의 가장 가까운 일정을 요청하면 일정 상세 정보와 상태코드 200을 반환한다.")
    @Test
    void show_upcoming_event() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        saveEvents(token, List.of(event1, event2), (long) meetingId);

        given(serverTimeManager.getDate())
                .willReturn(LocalDate.of(2022, 7, 31));
        given(serverTimeManager.calculateOpeningTime(event1.getEntranceTime()))
                .willReturn(event1.getEntranceTime().minusMinutes(30));
        given(serverTimeManager.calculateClosingTime(event1.getEntranceTime()))
                .willReturn(event1.getEntranceTime().plusMinutes(5));

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/events/upcoming", token);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", equalTo(1))
                .body("attendanceOpenTime", equalTo("09:30"))
                .body("attendanceClosedTime", equalTo("10:05"))
                .body("meetingStartTime", equalTo("10:00"))
                .body("meetingEndTime", equalTo("18:00"))
                .body("date", equalTo("2022-08-01"));
    }

    @DisplayName("모임의 가장 가까운 일정이 존재하지 않은 경우에  일정 상세 정보와 상태코드 404를 반환한다.")
    @Test
    void showUpcomingEvent_ifEventNotFound() {
        // given
        final User user1 = KUN.create();
        final User user2 = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(user2));

        final String token = signUpAndGetToken(user1);
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);

        final Event event1 = EVENT1.create(meeting);
        final Event event2 = EVENT2.create(meeting);
        saveEvents(token, List.of(event1, event2), (long) meetingId);

        given(serverTimeManager.getDate())
                .willReturn(LocalDate.of(2022, 8, 3));

        // when
        final ValidatableResponse response = get("/meetings/" + meetingId + "/events/upcoming", token);

        // then
        response.statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("일정이 존재하지 않습니다."));
    }
}
