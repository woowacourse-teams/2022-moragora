package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.AttendanceResponse;
import com.woowacourse.moragora.dto.AttendancesResponse;
import com.woowacourse.moragora.dto.CoffeeStatsResponse;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.MeetingAttendances;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.InvalidCoffeeTimeException;
import com.woowacourse.moragora.exception.event.EventNotFoundException;
import com.woowacourse.moragora.exception.meeting.AttendanceNotFoundException;
import com.woowacourse.moragora.exception.meeting.ClosingTimeExcessException;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import com.woowacourse.moragora.repository.AttendanceRepository;
import com.woowacourse.moragora.repository.EventRepository;
import com.woowacourse.moragora.repository.MeetingRepository;
import com.woowacourse.moragora.repository.ParticipantRepository;
import com.woowacourse.moragora.repository.UserRepository;
import com.woowacourse.moragora.support.ServerTimeManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AttendanceService {

    private final MeetingRepository meetingRepository;
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final ServerTimeManager serverTimeManager;

    public AttendanceService(final MeetingRepository meetingRepository,
                             final EventRepository eventRepository,
                             final ParticipantRepository participantRepository,
                             final AttendanceRepository attendanceRepository,
                             final UserRepository userRepository,
                             final ServerTimeManager serverTimeManager) {
        this.meetingRepository = meetingRepository;
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.serverTimeManager = serverTimeManager;
    }

    public AttendancesResponse findTodayAttendancesByMeeting(final Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        final LocalDate date = serverTimeManager.getDate();
        final Event event = eventRepository.findByMeetingIdAndDate(meetingId, date)
                .orElseThrow(() -> new ClientRuntimeException("오늘의 일정이 존재하지 않아 출석부를 조회할 수 없습니다.",
                        HttpStatus.BAD_REQUEST));

        final List<Attendance> attendances = attendanceRepository.findByParticipantIdInAndEventId(
                meeting.getParticipantIds(), event.getId());
        final List<AttendanceResponse> attendanceResponses = attendances.stream()
                .map(AttendanceResponse::from)
                .collect(Collectors.toList());

        return new AttendancesResponse(attendanceResponses);
    }

    @Transactional
    public void updateAttendance(final Long meetingId,
                                 final Long userId,
                                 final UserAttendanceRequest request) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        final User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        final Participant participant = participantRepository.findByMeetingIdAndUserId(meeting.getId(), user.getId())
                .orElseThrow(ParticipantNotFoundException::new);
        validateAttendanceTime(meeting);

        final Event event = eventRepository.findByMeetingIdAndDate(meetingId, serverTimeManager.getDate())
                .orElseThrow(EventNotFoundException::new);
        final Attendance attendance = attendanceRepository
                .findByParticipantIdAndEventId(participant.getId(), event.getId())
                .orElseThrow(AttendanceNotFoundException::new);

        if (request.getIsPresent()) {
            attendance.changeAttendanceStatus(Status.PRESENT);
            return;
        }
        attendance.changeAttendanceStatus(Status.NONE);
    }

    public CoffeeStatsResponse countUsableCoffeeStack(final Long meetingId) {
        final LocalDate today = serverTimeManager.getDate();
        final Event event = eventRepository.findByMeetingIdAndDate(meetingId, today)
                .orElse(null);
        final boolean isOver = Objects.isNull(event) || serverTimeManager.isOverClosingTime(event.getEntranceTime());

        final MeetingAttendances meetingAttendances = findMeetingAttendancesBy(meetingId);
        validateEnoughTardyCountToDisable(meetingAttendances, isOver, today);
        final Map<User, Long> userCoffeeStats = meetingAttendances.countUsableAttendancesPerUsers();
        return CoffeeStatsResponse.from(userCoffeeStats);
    }

    @Transactional
    public void disableUsedTardy(final Long meetingId) {
        final LocalDate today = serverTimeManager.getDate();
        final Event event = eventRepository.findByMeetingIdAndDate(meetingId, today)
                .orElse(null);
        final boolean isOver = Objects.isNull(event) || serverTimeManager.isOverClosingTime(event.getEntranceTime());

        final MeetingAttendances meetingAttendances = findMeetingAttendancesBy(meetingId);
        validateEnoughTardyCountToDisable(meetingAttendances, isOver, today);
        meetingAttendances.disableAttendances();
    }

    private void validateAttendanceTime(final Meeting meeting) {
        final Event event = eventRepository.findByMeetingIdAndDate(meeting.getId(), serverTimeManager.getDate())
                .orElseThrow(EventNotFoundException::new);
        final LocalTime entranceTime = event.getEntranceTime();

        if (serverTimeManager.isOverClosingTime(entranceTime)) {
            throw new ClosingTimeExcessException();
        }
    }

    private MeetingAttendances findMeetingAttendancesBy(final Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        final List<Long> participantIds = meeting.getParticipantIds();
        final List<Attendance> attendances = attendanceRepository.findByParticipantIdIn(participantIds);
        return new MeetingAttendances(attendances, participantIds.size());
    }

    private void validateEnoughTardyCountToDisable(final MeetingAttendances attendances,
                                                   final boolean isOver,
                                                   final LocalDate today) {
        if (!attendances.isTardyStackFull(isOver, today)) {
            throw new InvalidCoffeeTimeException();
        }
    }
}
