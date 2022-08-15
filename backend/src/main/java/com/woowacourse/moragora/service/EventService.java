package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.repository.AttendanceRepository;
import com.woowacourse.moragora.repository.EventRepository;
import com.woowacourse.moragora.repository.MeetingRepository;
import com.woowacourse.moragora.support.ServerTimeManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EventService {

    @PersistenceContext
    private EntityManager entityManager;

    private final Map<Attendance, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final TaskScheduler taskScheduler;
    private final EventRepository eventRepository;
    private final MeetingRepository meetingRepository;
    private final AttendanceRepository attendanceRepository;
    private final ServerTimeManager serverTimeManager;
    private final Scheduler scheduler;

    public EventService(final TaskScheduler taskScheduler,
                        final EventRepository eventRepository,
                        final MeetingRepository meetingRepository,
                        final AttendanceRepository attendanceRepository,
                        final ServerTimeManager serverTimeManager,
                        final Scheduler scheduler) {
        this.taskScheduler = taskScheduler;
        this.eventRepository = eventRepository;
        this.meetingRepository = meetingRepository;
        this.attendanceRepository = attendanceRepository;
        this.serverTimeManager = serverTimeManager;
        this.scheduler = scheduler;
    }

    @Transactional
    public void save(final EventsRequest request, final Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        final List<Event> events = request.toEntities(meeting).stream()
                .filter(event -> event.dateAfterOrEqualTo(serverTimeManager.getDate()))
                .collect(Collectors.toList());
        validateDuplicatedEventDate(events);
        validateAttendanceStartTimeIsAfterNow(events);

        eventRepository.saveAll(events);
        final List<Attendance> attendances = saveAllAttendances(meeting.getParticipants(), events);
        scheduleAttendancesUpdate(attendances);
    }

    private void validateDuplicatedEventDate(final List<Event> events) {
        final List<LocalDate> dates = events.stream()
                .map(Event::getDate)
                .distinct()
                .collect(Collectors.toList());
        if (dates.size() != events.size()) {
            throw new ClientRuntimeException("하루에 복수의 일정을 생성할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateAttendanceStartTimeIsAfterNow(final List<Event> events) {
        final Optional<Event> event = events.stream()
                .filter(it -> it.isSameDate(serverTimeManager.getDate()))
                .findAny();
        event.ifPresent(it -> {
            if (serverTimeManager.isAfterAttendanceStartTime(it.getEntranceTime())) {
                throw new ClientRuntimeException("출석 시간 전에 일정을 생성할 수 없습니다.", HttpStatus.BAD_REQUEST);
            }
        });
    }

    private void scheduleAttendancesUpdate(final List<Attendance> attendances) {
        attendances.forEach(attendance -> {
            final ScheduledFuture<?> schedule = taskScheduler.schedule(
                    () -> scheduler.updateToTardyAfterClosedTime(attendance),
                    calculateUpdateInstant(attendance.getEvent()));
            scheduledTasks.put(attendance, schedule);
        });
    }

    private List<Attendance> saveAllAttendances(final List<Participant> participants, final List<Event> events) {
        final List<Attendance> attendances = events.stream()
                .map(event -> createAttendances(participants, event))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return attendanceRepository.saveAll(attendances);
    }

    private List<Attendance> createAttendances(final List<Participant> participants, final Event event) {
        return participants.stream()
                .map(participant -> new Attendance(Status.NONE, false, participant, event))
                .collect(Collectors.toList());
    }

    private Instant calculateUpdateInstant(final Event event) {
        final LocalTime entranceTime = event.getEntranceTime();
        final LocalTime closingTime = serverTimeManager.calculateClosingTime(entranceTime);
        return closingTime.atDate(event.getDate()).
                atZone(ZoneId.systemDefault()).toInstant();
    }
}
