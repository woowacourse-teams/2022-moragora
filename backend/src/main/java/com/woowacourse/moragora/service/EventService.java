package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.EventCancelRequest;
import com.woowacourse.moragora.dto.EventResponse;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.dto.EventsResponse;
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
import com.woowacourse.moragora.support.ServerTimeManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
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

    private static final int SCHEDULING_SUBTRACT_TIME = 1;

    private final Map<Event, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final TaskScheduler taskScheduler;
    private final EventRepository eventRepository;
    private final MeetingRepository meetingRepository;
    private final AttendanceRepository attendanceRepository;
    private final ServerTimeManager serverTimeManager;


    public EventService(final TaskScheduler taskScheduler,
                        final EventRepository eventRepository,
                        final MeetingRepository meetingRepository,
                        final AttendanceRepository attendanceRepository,
                        final ServerTimeManager serverTimeManager) {
        this.taskScheduler = taskScheduler;
        this.eventRepository = eventRepository;
        this.meetingRepository = meetingRepository;
        this.attendanceRepository = attendanceRepository;
        this.serverTimeManager = serverTimeManager;
    }

    @Transactional
    public void save(final EventsRequest request, final Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        final List<Event> events = request.toEntities(meeting);
        eventRepository.saveAll(events);
        saveAllAttendances(meeting.getParticipants(), events);
    }

    private void saveAllAttendances(final List<Participant> participants, final List<Event> events) {
        final List<Attendance> attendances = events.stream()
                .map(event -> createAttendances(participants, event))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        attendanceRepository.saveAll(attendances);
    }

    private List<Attendance> createAttendances(List<Participant> participants, Event event) {
        return participants.stream()
                .map(participant -> new Attendance(Status.NONE, false, participant, event))
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancel(final EventCancelRequest request, final Long meetingId) {
        meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        final List<LocalDate> dates = request.getDates();
        List<Event> events = eventRepository.findByMeetingIdAndDateIn(meetingId, dates);
        final List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        attendanceRepository.deleteByEventIdIn(eventIds);
        eventRepository.deleteByIdIn(eventIds);
    }

    public EventResponse findUpcomingEvent(final Long meetingId) {
        final Event event = eventRepository.findFirstByMeetingIdAndDateGreaterThanEqualOrderByDate(
                meetingId, serverTimeManager.getDate())
                .orElseThrow(EventNotFoundException::new);

        final LocalTime entranceTime = event.getStartTime();
        final LocalTime attendanceOpenTime = serverTimeManager.calculateOpeningTime(entranceTime);
        final LocalTime attendanceClosedTime = serverTimeManager.calculateClosingTime(entranceTime);
        return EventResponse.of(event, attendanceOpenTime, attendanceClosedTime);
    }

    public EventsResponse findByDuration(final Long meetingId, final LocalDate begin, final LocalDate end) {
        List<Event> events = eventRepository.findByMeetingIdAndDuration(meetingId, begin, end);
        final List<EventResponse> eventResponses = events.stream()
                .map(event -> EventResponse.of(event, serverTimeManager.calculateOpeningTime(event.getStartTime()),
                        serverTimeManager.calculateClosingTime(event.getStartTime()))
                )
                .collect(Collectors.toList());

        return new EventsResponse(eventResponses);
    }
}
