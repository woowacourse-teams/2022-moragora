package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.MeetingAttendances;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.InvalidCoffeeTimeException;
import com.woowacourse.moragora.exception.meeting.AttendanceNotFoundException;
import com.woowacourse.moragora.exception.meeting.ClosingTimeExcessException;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import com.woowacourse.moragora.repository.AttendanceRepository;
import com.woowacourse.moragora.repository.MeetingRepository;
import com.woowacourse.moragora.repository.ParticipantRepository;
import com.woowacourse.moragora.repository.UserRepository;
import com.woowacourse.moragora.support.ServerTimeManager;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AttendanceService {

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final ServerTimeManager serverTimeManager;

    public AttendanceService(final MeetingRepository meetingRepository,
                             final ParticipantRepository participantRepository,
                             final AttendanceRepository attendanceRepository,
                             final UserRepository userRepository,
                             final ServerTimeManager serverTimeManager) {
        this.meetingRepository = meetingRepository;
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

        validateAttendanceTime(meeting);

        final Participant participant = participantRepository.findByMeetingIdAndUserId(meeting.getId(), user.getId())
                .orElseThrow(ParticipantNotFoundException::new);

        final Attendance attendance = attendanceRepository
                .findByParticipantIdAndAttendanceDate(participant.getId(), serverTimeManager.getDate())
                .orElseThrow(AttendanceNotFoundException::new);

        attendance.changeAttendanceStatus(request.getAttendanceStatus());
    }

    @Transactional
    public void disableUsedTardy(final Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        final List<Participant> participants = meeting.getParticipants();
        final List<Long> participantIds = participants.stream()
                .map(Participant::getId)
                .collect(Collectors.toList());
        final List<Attendance> attendances = attendanceRepository.findByParticipantIdIn(participantIds);
        final MeetingAttendances meetingAttendances = new MeetingAttendances(attendances, participants.size());
        validateEnoughTardyCountToDisable(meetingAttendances);
        meetingAttendances.disableAttendances(participants.size());
    }

    private void validateAttendanceTime(final Meeting meeting) {
        final LocalTime entranceTime = meeting.getEntranceTime();

        if (serverTimeManager.isOverClosingTime(entranceTime)) {
            throw new ClosingTimeExcessException();
        }
    }

    private void validateEnoughTardyCountToDisable(final MeetingAttendances attendances) {
        if (!attendances.isTardyStackFull()) {
            throw new InvalidCoffeeTimeException();
        }
    }
}
