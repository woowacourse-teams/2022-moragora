package com.woowacourse.moragora.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public class Events {

    private final List<Event> events;

    public Events(final List<Event> events) {
        this.events = events;
    }

    public List<Event> saveAll(final List<Event> newEvents) {
        for (Event newEvent : newEvents) {
            save(newEvent);
        }

        return events;
    }

    public void save(final Event newEvent) {
        final LocalDate date = newEvent.getDate();
        final Meeting meeting = newEvent.getMeeting();

        final Optional<Event> sameEvent = events.stream()
                .filter(event -> event.isSameDate(date) && event.isSameMeeting(meeting))
                .findAny();

        sameEvent.ifPresent(event -> event.changeTime(newEvent.getEntranceTime(), newEvent.getLeaveTime()));

        events.add(newEvent);
    }
}
