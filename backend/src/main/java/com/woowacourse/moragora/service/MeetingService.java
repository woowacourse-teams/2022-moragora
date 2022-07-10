package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.dto.UserAttendancesRequest;
import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.repository.AttendanceRepository;
import com.woowacourse.moragora.repository.MeetingRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final AttendanceRepository attendanceRepository;

    public MeetingService(final MeetingRepository meetingRepository, final AttendanceRepository attendanceRepository) {
        this.meetingRepository = meetingRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Transactional
    public Long save(final MeetingRequest request) {
        final Meeting meeting = request.toEntity();
        final Meeting savedMeeting = meetingRepository.save(meeting);
        return savedMeeting.getId();
    }

    public MeetingResponse findById(final Long id) {
        final Meeting meeting = meetingRepository.findById(id).get();
        return MeetingResponse.from(meeting);
    }

    @Transactional
    public void updateAttendance(final Long meetingId, final UserAttendancesRequest requests) {
        final Meeting meeting = meetingRepository.findById(meetingId).get();
        meeting.increaseMeetingCount();

        final List<Long> absentUserIds = requests.getUsers().stream()
                .filter(UserAttendanceRequest::getIsTardy)
                .map(UserAttendanceRequest::getId)
                .collect(Collectors.toList());

        List<Attendance> attendances = attendanceRepository.findByMeetingId(meetingId);
        for (final Attendance attendance : attendances) {
            if (absentUserIds.contains(attendance.getUser().getId())) {
                // TODO update (1 + N)
                attendance.increaseTardyCount();
            }
        }
    }
}
