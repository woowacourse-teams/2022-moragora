package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.User;
import com.woowacourse.moragora.repository.MeetingRepository;
import com.woowacourse.moragora.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    public MeetingService(final UserRepository userRepository,
                          final MeetingRepository meetingRepository) {
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
    }

    public MeetingResponse findById(final Long id) {
        final Meeting meeting = meetingRepository.findById(id).get();
        return MeetingResponse.from(meeting);
    }

    @Transactional
    public void updateAttendance(final Long meetingId, final List<UserAttendanceRequest> requests) {
        final Meeting meeting = meetingRepository.findById(meetingId).get();
        meeting.increaseMeetingCount();

        final List<User> users = userRepository.findByMeetingId(meetingId);

        final List<Long> absentUserIds = requests.stream()
                .filter(UserAttendanceRequest::getIsAbsent)
                .map(UserAttendanceRequest::getId)
                .collect(Collectors.toList());

        for (final User user : users) {
            if (absentUserIds.contains(user.getId())) {
                user.increaseAbsentCount();
            }
        }
    }
}
