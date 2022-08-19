package com.woowacourse.moragora.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public class Events {

    private final List<Event> events;

    public Events(final List<Event> events) {
        this.events = events;
    }

    public List<Event> updateAndExtractNewEvents(final List<Event> insertedEvents) {
        final ArrayList<Event> newEvents = new ArrayList<>();
        for (Event insertedEvent : insertedEvents) {
            updateOrExtractNewEvent(insertedEvent, newEvents);
        }
        return newEvents;
    }

    public void updateOrExtractNewEvent(final Event insertedEvent, final List<Event> newEvents) {
        final LocalDate date = insertedEvent.getDate();
        final Meeting meeting = insertedEvent.getMeeting();

        final Optional<Event> sameEvent = events.stream()
                .filter(event -> event.isSameDate(date) && event.isSameMeeting(meeting))
                .findAny();

        if (sameEvent.isPresent()) {
            sameEvent.get().updateTime(insertedEvent.getStartTime(), insertedEvent.getEndTime());
            return;
        }

        newEvents.add(insertedEvent);
    }
}
