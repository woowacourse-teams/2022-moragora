package com.woowacourse.moragora.support;

import com.woowacourse.moragora.domain.attendance.Attendance;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.attendance.Status;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.domain.attendance.AttendanceRepository;
import com.woowacourse.moragora.domain.event.EventRepository;
import com.woowacourse.moragora.domain.meeting.MeetingRepository;
import com.woowacourse.moragora.domain.participant.ParticipantRepository;
import com.woowacourse.moragora.domain.user.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DataSupport {

    private final UserRepository userRepository;

    private final MeetingRepository meetingRepository;

    private final ParticipantRepository participantRepository;

    private final AttendanceRepository attendanceRepository;

    private final EventRepository eventRepository;

    public DataSupport(final UserRepository userRepository,
                       final MeetingRepository meetingRepository,
                       final ParticipantRepository participantRepository,
                       final AttendanceRepository attendanceRepository,
                       final EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
        this.participantRepository = participantRepository;
        this.attendanceRepository = attendanceRepository;
        this.eventRepository = eventRepository;
    }

    public Participant saveParticipant(final User user, final Meeting meeting) {
        return saveParticipant(user, meeting, false);
    }

    public Participant saveParticipant(final User user, final Meeting meeting, final boolean isMaster) {
        final User savedUser = userRepository.save(user);
        final Meeting savedMeeting = meetingRepository.save(meeting);
        final Participant participant = participantRepository.save(new Participant(savedUser, savedMeeting, isMaster));
        participant.mapMeeting(savedMeeting);
        return participant;
    }

    public Attendance saveAttendance(final Participant participant, final Event event, final Status status) {
        return attendanceRepository.save(new Attendance(status, false, participant, event));
    }


    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    public List<Long> saveUsers(final List<User> users) {
        for (User user : users) {
            userRepository.save(user);
        }

        return users.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    public Meeting saveMeeting(final Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public Event saveEvent(final Event event) {
        return eventRepository.save(event);
    }

    public Optional<Attendance> findAttendanceByParticipantAndEvent(final Participant participant, final Event event) {
        return attendanceRepository.findByParticipantIdAndEventId(participant.getId(), event.getId());
    }
}
