package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class EventsRequest {

    private List<EventRequest> events;

    @Builder
    public EventsRequest(final List<EventRequest> events) {
        this.events = events;
    }

    public List<Event> toEntities(final Meeting meeting) {
        return events.stream()
                .map(event -> event.toEntity(meeting))
                .collect(Collectors.toList());
    }
}
