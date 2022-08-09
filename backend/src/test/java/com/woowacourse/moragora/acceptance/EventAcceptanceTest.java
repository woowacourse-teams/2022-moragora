package com.woowacourse.moragora.acceptance;

import static com.woowacourse.moragora.support.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.AZPI;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.MASTER;
import static com.woowacourse.moragora.support.UserFixtures.NO_MASTER;
import static com.woowacourse.moragora.support.UserFixtures.createUsers;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.moragora.dto.EventCancelRequest;
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

    @DisplayName("마스터 권한이 없는 참가자가 일정 등록을 요청하는 경우 상태코드 403을 반환한다.")
    @Test
    void add_NotMaster() {
        // given
        final User master = MASTER.create();
        final User noMaster = NO_MASTER.create();
        final User user = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(noMaster, user));

        final String masterToken = signUpAndGetToken(master);

        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(masterToken, userIds, meeting);

        final LoginRequest noMasterLoginRequest = new LoginRequest(noMaster.getEmail(), "1234asdf!");
        final String token = post("/login", noMasterLoginRequest)
                .extract().jsonPath().get("accessToken");

        final Event event1 = EVENT1.create(meeting);
        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getEntranceTime(),
                                event1.getLeaveTime(),
                                event1.getDate()
                        )
                ));
        // when
        final ValidatableResponse response = post("/meetings/" + meetingId + "/events", eventsRequest, token);

        // then
        response.statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("특정 모임에 대한 일정을 선택적으로 삭제하고 상태코드 204를 반환한다.")
    @Test
    void cancel() {
        // given
        final String token = signUpAndGetToken(MASTER.create());
        final List<Long> userIds = saveUsers(createUsers());
        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(token, userIds, meeting);
        saveEvents(token, List.of(EVENT1.create(meeting), EVENT2.create(meeting)), (long) meetingId);

        // when
        final EventCancelRequest eventCancelRequest = new EventCancelRequest(
                List.of(EVENT1.getDate(), EVENT2.getDate()));

        final ValidatableResponse response = delete("/meetings/" + meetingId + "/events", eventCancelRequest, token);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("마스터가 아닌 참가자가 일정 삭제 요청을 하면 상태코드 403을 반환한다.")
    @Test
    void cancel_NotMaster() {
        // given
        final User master = MASTER.create();
        final User noMaster = NO_MASTER.create();
        final User user = AZPI.create();
        final List<Long> userIds = saveUsers(List.of(noMaster, user));

        final String masterToken = signUpAndGetToken(master);

        final Meeting meeting = MORAGORA.create();
        final int meetingId = saveMeeting(masterToken, userIds, meeting);

        final LoginRequest noMasterLoginRequest = new LoginRequest(noMaster.getEmail(), "1234asdf!");
        final String token = post("/login", noMasterLoginRequest)
                .extract().jsonPath().get("accessToken");

        final Event event1 = EVENT1.create(meeting);
        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                event1.getEntranceTime(),
                                event1.getLeaveTime(),
                                event1.getDate()
                        )
                ));
        post("/meetings/" + meetingId + "/events", eventsRequest, masterToken);

        // when
        final EventCancelRequest eventCancelRequest = new EventCancelRequest(
                List.of(EVENT1.getDate(), EVENT2.getDate()));

        final ValidatableResponse response = delete("/meetings/" + meetingId + "/events", eventCancelRequest, token);

        // then
        response.statusCode(HttpStatus.FORBIDDEN.value());
    }
}
