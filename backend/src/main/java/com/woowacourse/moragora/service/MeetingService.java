package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.UserAttendancesRequest;
import com.woowacourse.moragora.dto.UserResponse;
import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.MeetingNotFoundException;
import com.woowacourse.moragora.exception.UserNotFoundException;
import com.woowacourse.moragora.repository.AttendanceRepository;
import com.woowacourse.moragora.repository.MeetingRepository;
import com.woowacourse.moragora.repository.ParticipantRepository;
import com.woowacourse.moragora.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public MeetingService(final MeetingRepository meetingRepository,
                          final ParticipantRepository participantRepository,
                          final AttendanceRepository attendanceRepository,
                          final UserRepository userRepository) {
        this.meetingRepository = meetingRepository;
        this.participantRepository = participantRepository;
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    // TODO: for 문 안에 insert문 한번에 날리는 것 고려
    // TODO: master 지정해야함
    @Transactional
    public Long save(final MeetingRequest request, final Long userId) {
        final User loginUser = findUser(userId);
        final Meeting meeting = meetingRepository.save(request.toEntity());

        final Participant participant = new Participant(loginUser, meeting);
        participantRepository.save(participant);

        final List<User> users = userRepository.findByIds(request.getUserIds());
        for (User user : users) {
            participantRepository.save(new Participant(user, meeting));
        }

        return meeting.getId();
    }

    // TODO 1 + N 최적화 대상
    @Transactional
    public MeetingResponse findById(final Long meetingId, final Long userId) {
        final Meeting meeting = findMeeting(meetingId);
        final List<Participant> participants = participantRepository.findByMeetingId(meeting.getId());

        putAttendanceIfAbsent(participants);

        List<UserResponse> userResponses = new ArrayList<>();

        for (final Participant participant : participants) {
            final List<Attendance> attendances = attendanceRepository.findByParticipantId(participant.getId());

            final int tardyCount = (int) attendances.stream()
                    .filter(Attendance::isTardy)
                    .count();

            final User foundUser = participant.getUser();
            final UserResponse userResponse = new UserResponse(foundUser.getId(), foundUser.getEmail(),
                    foundUser.getNickname(), tardyCount);

            userResponses.add(userResponse);
        }

        return MeetingResponse.of(meeting, userResponses);
    }

    // TODO update (1 + N) -> 최적하기
    // TODO 출석 제출할 때 구현 예정
    @Transactional
    public void updateAttendance(final Long meetingId, final UserAttendancesRequest requests) {
/*
        final Meeting meeting = findMeeting(meetingId);
        meeting.increaseMeetingCount();

        final List<Long> absentUserIds = requests.getUsers().stream()
                .filter(UserAttendanceRequest::getIsTardy)
                .map(UserAttendanceRequest::getId)
                .collect(Collectors.toList());

        final List<Attendance> attendances = attendanceRepository.findByMeetingId(meetingId);
        for (final Attendance attendance : attendances) {
            if (absentUserIds.contains(attendance.getUser().getId())) {
                attendance.increaseTardyCount();
            }
        }
*/
    }

    private Meeting findMeeting(final Long id) {
        return meetingRepository.findById(id)
                .orElseThrow(MeetingNotFoundException::new);
    }

    private User findUser(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    private void putAttendanceIfAbsent(final List<Participant> participants) {
        final Participant anyParticipant = participants.get(0);
        final LocalDate today = LocalDate.now();
        Optional<Attendance> attendance = attendanceRepository
                .findByParticipantIdAndAttendanceDate(anyParticipant.getId(), today);

        if (attendance.isEmpty()) {
            saveAttendance(participants, today);
        }
    }

    private void saveAttendance(final List<Participant> participants, final LocalDate today) {
        for (Participant participant : participants) {
            attendanceRepository.save(new Attendance(participant, today, true));
        }
    }
}
