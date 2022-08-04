package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.exception.event.EventNotFoundException;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.repository.AttendanceRepository;
import com.woowacourse.moragora.repository.EventRepository;
import com.woowacourse.moragora.repository.MeetingRepository;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EventService {

    private static final int SCHEDULING_SUBTRACT_TIME = 30;

    private final Map<Event, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final TaskScheduler taskScheduler;
    private final EventRepository eventRepository;
    private final MeetingRepository meetingRepository;
    private final AttendanceRepository attendanceRepository;

    public EventService(final TaskScheduler taskScheduler,
                        final EventRepository eventRepository,
                        final MeetingRepository meetingRepository,
                        final AttendanceRepository attendanceRepository) {
        this.taskScheduler = taskScheduler;
        this.eventRepository = eventRepository;
        this.meetingRepository = meetingRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Transactional
    public List<Event> save(final EventsRequest request, final Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        final List<Event> events = request.toEntities(meeting);
        return eventRepository.saveAll(events);
    }

    @Transactional
    public void saveSchedules(final List<Event> events) {
        events.forEach(event -> {
            ScheduledFuture<?> task = taskScheduler.schedule(
                    () -> saveAttendances(event), Date.from(getInstant(event)));
            scheduledTasks.put(event, task);
        });
    }

    @Transactional
    public void saveAttendances(final Event event) {
        final Event foundEvent = eventRepository.findById(event.getId())
                .orElseThrow(EventNotFoundException::new);
        final Meeting meeting = foundEvent.getMeeting();
        final List<Participant> participants = meeting.getParticipants();
        final List<Attendance> attendances = participants.stream()
                .map(participant -> new Attendance(Status.TARDY, false, participant, foundEvent))
                .collect(Collectors.toList());
        attendanceRepository.saveAll(attendances);
    }

    private Instant getInstant(final Event event) {
        final LocalTime localTime = event.getEntranceTime().minusMinutes(SCHEDULING_SUBTRACT_TIME);
        return localTime.atDate(event.getDate()).
                atZone(ZoneId.systemDefault()).toInstant();
    }
}
