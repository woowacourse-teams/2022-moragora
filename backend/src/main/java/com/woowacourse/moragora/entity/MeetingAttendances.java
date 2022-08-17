package com.woowacourse.moragora.entity;

import com.woowacourse.moragora.entity.user.User;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MeetingAttendances {

    private final List<Attendance> values;
    private final int numberOfParticipants;

    public MeetingAttendances(final List<Attendance> values, final int numberOfParticipants) {
        validateSingleMeeting(values);
        this.values = values;
        this.numberOfParticipants = numberOfParticipants;
    }

    public ParticipantAttendances extractAttendancesByParticipant(final Participant participant) {
        final List<Attendance> attendances = values.stream()
                .filter(attendance -> attendance.getParticipant().equals(participant))
                .collect(Collectors.toList());
        return new ParticipantAttendances(attendances);
    }

    public int countTardy() {
        return (int) values.stream()
                .filter(Attendance::isEnabled)
                .filter(Attendance::isTardy).count();
    }

    public boolean isTardyStackFull() {
        return countTardy() >= numberOfParticipants;
    }

    public Map<User, Long> countUsableAttendancesPerUsers() {
        return filterUsableAttendances().stream()
                .collect(Collectors.groupingBy(
                        attendance -> attendance.getParticipant().getUser(),
                        HashMap::new,
                        Collectors.counting()
                ));
    }

    public void disableAttendances() {
        filterUsableAttendances().forEach(Attendance::disable);
    }

    private void validateSingleMeeting(final List<Attendance> value) {
        final long meetingCount = value.stream()
                .map(Attendance::getParticipant)
                .map(Participant::getMeeting)
                .distinct()
                .count();

        if (meetingCount > 1) {
            throw new IllegalArgumentException("한 미팅에 대한 참가자가 아닙니다.");
        }
    }

    private List<Attendance> filterUsableAttendances() {
        return values.stream()
                .filter(Attendance::isEnabled)
                .filter(Attendance::isTardy)
                .sorted(Comparator.comparing(attendance -> attendance.getEvent().getDate()))
                .limit(numberOfParticipants)
                .collect(Collectors.toList());
    }
}
