package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.CoffeeStatsResponse;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.MeetingAttendances;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.user.User;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
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

        attendance.changeAttendanceStatus(request.getAttendanceStatus());
    }

    public CoffeeStatsResponse countUsableCoffeeStack(final Long meetingId) {
        final MeetingAttendances meetingAttendances = findMeetingAttendancesBy(meetingId);
        validateEnoughTardyCountToDisable(meetingAttendances);
        final Map<User, Long> userCoffeeStats = meetingAttendances.countUsableAttendancesPerUsers();
        return CoffeeStatsResponse.from(userCoffeeStats);
    }

    @Transactional
    public void disableUsedTardy(final Long meetingId) {
        final MeetingAttendances meetingAttendances = findMeetingAttendancesBy(meetingId);
        validateEnoughTardyCountToDisable(meetingAttendances);
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

    private void validateEnoughTardyCountToDisable(final MeetingAttendances attendances) {
        if (!attendances.isTardyStackFull()) {
            throw new InvalidCoffeeTimeException();
        }
    }
}
