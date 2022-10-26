package com.woowacourse.moragora.domain.query;

import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.meeting.MeetingRepository;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.ParticipantRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CompositionRepository {

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final AttendanceRepository attendanceRepository;

    public CompositionRepository(final MeetingRepository meetingRepository,
                                 final ParticipantRepository participantRepository,
                                 final AttendanceRepository attendanceRepository) {
        this.meetingRepository = meetingRepository;
        this.participantRepository = participantRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<Meeting> meetingsWithTardyCount(final Long userId) {
        final List<Meeting> meetings = meetingRepository.findAllByUserId(userId);

        for (final Meeting meeting : meetings) {
            final List<Participant> tardyCountParticipants =
                    participantRepository.countParticipantsTardy(meeting.getParticipants());
            meeting.mapParticipants(tardyCountParticipants);
            meeting.calculateTardy();
        }

        return meetings;
    }
}
