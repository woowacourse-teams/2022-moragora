package com.woowacourse.moragora.acceptance;

import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.dto.MeetingRequest;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class EventAcceptanceTest extends AcceptanceTest {

    @DisplayName("특정 모임에 대한 일정들을 등록하고 상태코드 200을 반환한다.")
    @Test
    void add() {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest(
                "모임1",
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)
        );

        final String token = signUpAndGetToken();
        final ValidatableResponse meetingResponse = post("/meetings", meetingRequest, token);
        final String location = meetingResponse.extract().header("Location").split("/")[2];
        final long meetingId = Long.parseLong(location);

        final EventsRequest eventsRequest = new EventsRequest(
                List.of(
                        new EventRequest(
                                LocalTime.of(10, 0),
                                LocalTime.of(18, 0),
                                LocalDate.of(2022, 8, 3)
                        ),
                        new EventRequest(
                                LocalTime.of(10, 0),
                                LocalTime.of(18, 0),
                                LocalDate.of(2022, 8, 4)
                        )
                ));

        // when
        final ValidatableResponse response = post("/meetings/" + meetingId + "/events", eventsRequest, token);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }
}
