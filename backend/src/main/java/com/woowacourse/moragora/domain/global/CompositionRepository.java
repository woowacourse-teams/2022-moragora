package com.woowacourse.moragora.domain.global;

import static java.util.stream.Collectors.toMap;

import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.meeting.MeetingRepository;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.TardyCountDto;
import com.woowacourse.moragora.domain.participant.ParticipantRepository;
import com.woowacourse.moragora.domain.user.UserRepository;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class CompositionRepository {

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public CompositionRepository(final MeetingRepository meetingRepository,
                                 final ParticipantRepository participantRepository,
                                 final AttendanceRepository attendanceRepository,
                                 final UserRepository userRepository) {
        this.meetingRepository = meetingRepository;
        this.participantRepository = participantRepository;
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    public Meeting meetingWithTardyCount(final Long meetingId) {
        final Meeting meeting = meetingRepository.findMeetingParticipantUsersById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        final Map<Long, Long> participantIdToTardyCount = findParticipantIdToTardyCount(meeting);

        allocateTardyCountToParticipants(meeting, participantIdToTardyCount);

        return meeting;
    }

    public List<Meeting> meetingsWithTardyCount(final Long userId) {
        final List<Meeting> meetings = meetingRepository.findMeetingParticipantsByUserId(userId);

        for (final Meeting meeting : meetings) {
            final Map<Long, Long> participantIdToTardyCount = findParticipantIdToTardyCount(meeting);
            allocateTardyCountToParticipants(meeting, participantIdToTardyCount);
        }

        return meetings;
    }

    private Map<Long, Long> findParticipantIdToTardyCount(final Meeting meeting) {
        final List<TardyCountDto> tardyCountDtos = participantRepository.countParticipantsTardy(meeting.getParticipants());

        return tardyCountDtos.stream()
                .collect(toMap(TardyCountDto::getParticipantId, TardyCountDto::getTardyCount));
    }

    private void allocateTardyCountToParticipants(final Meeting meeting, final Map<Long, Long> participantIdToTardyCount) {
        for (final Participant participant : meeting.getParticipants()) {
            final Long tardyCount = participantIdToTardyCount.get(participant.getId());
            participant.allocateTardyCount(tardyCount);
        }
    }
}
