package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.EventResponse;
import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingResponse;
import com.woowacourse.moragora.dto.MyMeetingsResponse;
import com.woowacourse.moragora.dto.ParticipantResponse;
import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.MeetingAttendances;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.ParticipantAttendances;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.exception.participant.InvalidParticipantException;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final ServerTimeManager serverTimeManager;

    public MeetingService(final MeetingRepository meetingRepository,
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
    public Long save(final MeetingRequest request, final Long loginId) {
        final Meeting meeting = meetingRepository.save(request.toEntity());
        final List<Long> userIds = request.getUserIds();
        validateUserIds(userIds, loginId);

        final User loginUser = userRepository.findById(loginId)
                .orElseThrow(UserNotFoundException::new);
        final List<User> users = userRepository.findByIdIn(userIds);
        validateUserExists(userIds, users);

        saveParticipants(meeting, loginUser, users);

        return meeting.getId();
    }

    @Transactional
    public MeetingResponse findById(final Long meetingId, final Long loginId) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);

        final List<Participant> participants = meeting.getParticipants();
        final Participant participant = participantRepository.findByMeetingIdAndUserId(meeting.getId(), loginId)
                .orElseThrow(ParticipantNotFoundException::new);

        final LocalDate today = serverTimeManager.getDate();
        final Optional<Event> event = eventRepository.findByMeetingIdAndDate(meeting.getId(), today);
        final boolean isActive = event.isPresent() && serverTimeManager.isAttendanceTime(event.get().getEntranceTime());
        final boolean isOver = event.isPresent() && serverTimeManager.isOverClosingTime(event.get().getEntranceTime());

        final MeetingAttendances meetingAttendances = findAttendancesByMeeting(meeting.getParticipantIds());
        final List<ParticipantResponse> participantResponses = participants.stream()
                .map(it -> generateParticipantResponse(today, meetingAttendances, isOver, it))
                .collect(Collectors.toList());

        final List<Event> attendedEvents = eventRepository.findByMeetingIdAndDateLessThanEqual(meetingId, today);
        return MeetingResponse.from(
                meeting, attendedEvents.size(), participant.getIsMaster(),
                meetingAttendances.isTardyStackFull(isOver, today),
                isActive, participantResponses
        );
    }

    public MyMeetingsResponse findAllByUserId(final Long userId) {
        final List<Participant> participants = participantRepository.findByUserId(userId);
        final List<MyMeetingResponse> myMeetingResponses = participants.stream()
                .map(participant -> generateMyMeetingResponse(participant, getMeetingAttendances(participant)))
                .collect(Collectors.toList());

        return new MyMeetingsResponse(myMeetingResponses);
    }

    private MeetingAttendances getMeetingAttendances(final Participant participant) {
        final Meeting meeting = participant.getMeeting();
        final List<Long> participantIds = meeting.getParticipantIds();
        return findAttendancesByMeeting(participantIds);
    }

    /**
     * 참가자 userIds 내부에 loginId가 있는지 검증해야 userIds.size()가 0인지 검증이 정상적으로 이루어집니다.
     */
    private void validateUserIds(final List<Long> userIds, final Long loginId) {
        if (Set.copyOf(userIds).size() != userIds.size()) {
            throw new InvalidParticipantException("참가자 명단에 중복이 있습니다.");
        }

        if (userIds.contains(loginId)) {
            throw new InvalidParticipantException("생성자가 참가자 명단에 포함되어 있습니다.");
        }

        if (userIds.size() == 0) {
            throw new InvalidParticipantException("생성자를 제외한 참가자가 없습니다.");
        }
    }

    private void validateUserExists(final List<Long> userIds, final List<User> users) {
        if (users.size() != userIds.size()) {
            throw new UserNotFoundException();
        }
    }

    private void saveParticipants(final Meeting meeting, final User loginUser, final List<User> users) {
        final Participant loginParticipant = new Participant(loginUser, meeting, true);
        final List<Participant> participants = users.stream()
                .map(user -> new Participant(user, meeting, false))
                .collect(Collectors.toList());
        participants.add(loginParticipant);

        for (Participant participant : participants) {
            participant.mapMeeting(meeting);
            participantRepository.save(participant);
        }
    }

    private MeetingAttendances findAttendancesByMeeting(final List<Long> participantIds) {
        final List<Attendance> foundAttendances = attendanceRepository.findByParticipantIdIn(participantIds);
        return new MeetingAttendances(foundAttendances, participantIds.size());
    }

    private ParticipantResponse generateParticipantResponse(final LocalDate now,
                                                            final MeetingAttendances meetingAttendances,
                                                            final boolean isOver,
                                                            final Participant participant) {
        final ParticipantAttendances participantAttendances =
                meetingAttendances.extractAttendancesByParticipant(participant);
        final int tardyCount = participantAttendances.countTardy(isOver, now);

        return ParticipantResponse.of(participant, tardyCount);
    }

    private MyMeetingResponse generateMyMeetingResponse(final Participant participant,
                                                        final MeetingAttendances meetingAttendances) {
        final Meeting meeting = participant.getMeeting();
        final ParticipantAttendances participantAttendances = meetingAttendances
                .extractAttendancesByParticipant(participant);
        final boolean isLoginUserMaster = participant.getIsMaster();

        final LocalDate today = serverTimeManager.getDate();
        final Optional<Event> upcomingEvent = eventRepository
                .findFirstByMeetingIdAndDateGreaterThanEqualOrderByDate(meeting.getId(), today);
        final boolean isActive =
                upcomingEvent.isPresent() && serverTimeManager.isAttendanceTime(upcomingEvent.get().getEntranceTime());

        final boolean isOver =
                upcomingEvent.isPresent() && serverTimeManager.isOverClosingTime(upcomingEvent.get().getEntranceTime());
        final boolean isCoffeeTime = meetingAttendances.isTardyStackFull(isOver, today);
        final int tardyCount = participantAttendances.countTardy(isOver, today);

        if (upcomingEvent.isEmpty()) {
            return MyMeetingResponse.of(
                    meeting, tardyCount, isLoginUserMaster, isCoffeeTime, isActive, null
            );
        }
        final Event event = upcomingEvent.get();
        final LocalTime entranceTime = event.getEntranceTime();
        final LocalTime attendanceOpenTime = serverTimeManager.calculateOpeningTime(entranceTime);
        final LocalTime attendanceClosedTime = serverTimeManager.calculateClosingTime(entranceTime);
        return MyMeetingResponse.of(
                meeting, tardyCount, isLoginUserMaster, isCoffeeTime, isActive,
                EventResponse.of(event, attendanceOpenTime, attendanceClosedTime)
        );
    }
}
