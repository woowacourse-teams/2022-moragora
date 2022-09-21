package com.woowacourse.moragora.support;


import com.woowacourse.moragora.application.EventService;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.event.EventRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SchedulerStartUp implements ApplicationListener<ContextRefreshedEvent> {

    private final EventService eventService;
    private final EventRepository eventRepository;

    public SchedulerStartUp(final EventService eventService,
                            final EventRepository eventRepository) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final LocalDate date = LocalDate.now();
        final List<Event> eventsToSchedule = eventRepository.findByDateGreaterThanEqual(date);
        eventService.initializeSchedules(eventsToSchedule);
    }
}
