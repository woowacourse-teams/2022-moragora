package com.woowacourse.moragora.support;

import com.woowacourse.moragora.application.ScheduledTasks;
import com.woowacourse.moragora.application.Scheduler;
import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.event.EventRepository;
import com.woowacourse.moragora.support.timestrategy.ServerDateTime;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class SchedulerStartUp implements ApplicationListener<ContextRefreshedEvent> {

    private final EventRepository eventRepository;
    private final ScheduledTasks scheduledTasks;
    private final TaskScheduler taskScheduler;
    private final Scheduler scheduler;


    public SchedulerStartUp(final EventRepository eventRepository,
                            final ScheduledTasks scheduledTasks,
                            final TaskScheduler taskScheduler,
                            final Scheduler scheduler) {
        this.eventRepository = eventRepository;
        this.scheduledTasks = scheduledTasks;
        this.taskScheduler = taskScheduler;
        this.scheduler = scheduler;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final LocalDate date = LocalDate.now();
        final List<Event> eventsToSchedule = eventRepository.findByDateGreaterThanEqual(date);
        scheduleAttendancesUpdateByEvents(eventsToSchedule);
    }

    public void scheduleAttendancesUpdateByEvents(final List<Event> newEvents) {
        newEvents.forEach(event -> {
            final ScheduledFuture<?> schedule = taskScheduler.schedule(
                    () -> scheduler.updateAttendancesToTardyAfterClosedTime(event),
                    calculateUpdateInstant(event));
            scheduledTasks.put(event, schedule);
        });
    }

    private Instant calculateUpdateInstant(final Event event) {
        final ServerTimeManager serverTimeManager = new ServerTimeManager(new ServerDateTime());
        final LocalTime startTime = event.getStartTime();
        final LocalTime closingTime = serverTimeManager.calculateAttendanceCloseTime(startTime);
        return closingTime.atDate(event.getDate()).
                atZone(ZoneId.systemDefault()).toInstant();
    }
}
