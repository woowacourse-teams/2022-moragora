package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EventsRequest {

    private List<EventRequest> events;

    public EventsRequest(final List<EventRequest> events) {
        this.events = events;
    }

    public List<Event> toEntities(final Meeting meeting) {
        return events.stream()
                .map(event -> event.toEntity(meeting))
                .collect(Collectors.toList());
    }
}
