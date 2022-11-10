package com.woowacourse.moragora.application;

import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.event.EventRepository;
import com.woowacourse.moragora.domain.geolocation.Beacon;
import com.woowacourse.moragora.domain.geolocation.BeaconRepository;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.meeting.MeetingRepository;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.ParticipantRepository;
import com.woowacourse.moragora.domain.query.CompositionRepository;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.domain.user.UserRepository;
import com.woowacourse.moragora.dto.request.meeting.BeaconRequest;
import com.woowacourse.moragora.dto.request.meeting.MasterRequest;
import com.woowacourse.moragora.dto.request.meeting.MeetingRequest;
import com.woowacourse.moragora.dto.request.meeting.MeetingUpdateRequest;
import com.woowacourse.moragora.dto.response.meeting.MeetingActiveResponse;
import com.woowacourse.moragora.dto.response.meeting.MeetingResponse;
import com.woowacourse.moragora.dto.response.meeting.MyMeetingResponse;
import com.woowacourse.moragora.dto.response.meeting.MyMeetingsResponse;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.beacon.BeaconNumberExceedException;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.exception.participant.InvalidParticipantException;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MeetingService {

    private static final int MAX_BEACON_SIZE = 3;

    private final MeetingRepository meetingRepository;
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final BeaconRepository beaconRepository;
    private final CompositionRepository compositionRepository;
    private final ServerTimeManager serverTimeManager;

    public MeetingService(final MeetingRepository meetingRepository,
                          final EventRepository eventRepository,
                          final ParticipantRepository participantRepository,
                          final AttendanceRepository attendanceRepository,
                          final UserRepository userRepository,
                          final BeaconRepository beaconRepository,
                          final CompositionRepository compositionRepository,
                          final ServerTimeManager serverTimeManager) {
        this.meetingRepository = meetingRepository;
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.beaconRepository = beaconRepository;
        this.compositionRepository = compositionRepository;
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
        validateUsersExists(userIds, users);

        saveParticipants(meeting, loginUser, users);

        return meeting.getId();
    }

    public MeetingResponse findById(final Long meetingId, final Long loginId) {
        final LocalDate today = serverTimeManager.getDate();
        final Meeting meeting = compositionRepository.meetingWithTardyCount(meetingId);

        final long attendedEventCount = eventRepository.countByMeetingIdAndDateLessThanEqual(meetingId, today);
        final Participant loginParticipant = meeting.findParticipant(loginId)
                .orElseThrow(ParticipantNotFoundException::new);

        return MeetingResponse.of(meeting, attendedEventCount, loginParticipant);
    }

    public MyMeetingsResponse findAllByMe(final Long loginUserId) {
        final LocalDate today = serverTimeManager.getDate();
        final List<Meeting> meetings = compositionRepository.meetingsWithTardyCount(loginUserId);

        final List<MyMeetingResponse> myMeetingsResponse = createMyMeetingsResponse(loginUserId, today, meetings);

        return new MyMeetingsResponse(myMeetingsResponse);
    }


    @Transactional
    public void assignMaster(final Long meetingId, final MasterRequest request, final Long loginId) {
        final Long assignedUserId = request.getUserId();
        validateMeetingExists(meetingId);
        validateUserExists(assignedUserId);
        validateAssignee(loginId, assignedUserId);

        final Participant assignedParticipant = participantRepository
                .findByMeetingIdAndUserId(meetingId, assignedUserId)
                .orElseThrow(ParticipantNotFoundException::new);
        final Participant masterParticipant = participantRepository.findByMeetingIdAndUserId(meetingId, loginId)
                .orElseThrow(ParticipantNotFoundException::new);

        assignedParticipant.updateIsMaster(true);
        masterParticipant.updateIsMaster(false);
    }

    @Transactional
    public void updateName(final MeetingUpdateRequest request, final Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        meeting.updateName(request.getName());
    }

    @Transactional
    public void deleteParticipant(final long meetingId, final long userId) {
        validateMeetingExists(meetingId);
        validateUserExists(userId);

        final Participant participant = participantRepository.findByMeetingIdAndUserId(meetingId, userId)
                .orElseThrow(ParticipantNotFoundException::new);
        validateNotMaster(participant);

        attendanceRepository.deleteByParticipantId(participant.getId());
        participantRepository.delete(participant);
    }

    @Transactional
    public void deleteMeeting(final Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);

        final List<Long> participantIds = meeting.getParticipantIds();

        attendanceRepository.deleteByParticipantIdIn(participantIds);
        participantRepository.deleteByIdIn(participantIds);
        eventRepository.deleteByMeetingId(meeting.getId());
        beaconRepository.deleteByMeetingId(meeting.getId());
        meetingRepository.deleteById(meeting.getId());
    }

    @Transactional
    public void addBeacons(final Long meetingId, final List<BeaconRequest> beaconsRequest) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);

        if (validateMaximumBeacon(beaconsRequest)) {
            throw new BeaconNumberExceedException();
        }

        final List<Beacon> beacons = beaconsRequest.stream()
                .map((BeaconRequest beaconRequest) -> beaconRequest.toEntity(meeting))
                .collect(Collectors.toList());

        beaconRepository.saveAll(beacons);
    }

    public MeetingActiveResponse checkActive(final Long meetingId) {
        validateMeetingExists(meetingId);

        final Optional<Event> event = eventRepository.findByMeetingIdAndDate(meetingId, serverTimeManager.getDate());
        final boolean isActive = event.isPresent() && serverTimeManager.isAttendanceOpen(event.get().getStartTime());

        return new MeetingActiveResponse(isActive);
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

    private List<MyMeetingResponse> createMyMeetingsResponse(final Long loginUserId,
                                                             final LocalDate today,
                                                             final List<Meeting> meetings) {
        final List<MyMeetingResponse> myMeetingsResponse = new ArrayList<>();

        for (final Meeting meeting : meetings) {

            final Event upcomingEvent = eventRepository
                    .findFirstByMeetingIdAndDateGreaterThanEqualOrderByDate(meeting.getId(), today)
                    .orElse(null);

            myMeetingsResponse.add(MyMeetingResponse.of(meeting, upcomingEvent, loginUserId, serverTimeManager));
        }

        return myMeetingsResponse;
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

        if (userIds.isEmpty()) {
            throw new InvalidParticipantException("생성자를 제외한 참가자가 없습니다.");
        }
    }

    private boolean validateMaximumBeacon(final List<BeaconRequest> beaconsRequest) {
        return beaconsRequest.size() > MAX_BEACON_SIZE;
    }

    private void validateUsersExists(final List<Long> userIds, final List<User> users) {
        if (users.size() != userIds.size()) {
            throw new UserNotFoundException();
        }
    }

    private void validateAssignee(final Long loginId, final Long participantId) {
        if (Objects.equals(loginId, participantId)) {
            throw new ClientRuntimeException("스스로에게 마스터 권한을 넘길 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateNotMaster(final Participant participant) {
        if (Boolean.TRUE.equals(participant.getIsMaster())) {
            throw new ClientRuntimeException("마스터는 모임을 나갈 수 없습니다.", HttpStatus.FORBIDDEN);
        }
    }

    private void validateMeetingExists(final Long meetingId) {
        if (!meetingRepository.existsById(meetingId)) {
            throw new MeetingNotFoundException();
        }
    }

    private void validateUserExists(final Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
    }
}
