package com.woowacourse.moragora.dto.request.event;

import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class EventsRequest {

    @Valid
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
