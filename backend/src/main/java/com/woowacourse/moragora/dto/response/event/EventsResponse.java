package com.woowacourse.moragora.dto.response.event;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EventsResponse {

    private final List<EventResponse> events;

    public EventsResponse(final List<EventResponse> events) {
        this.events = events;
    }
}
