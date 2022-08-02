package com.woowacourse.moragora.entity;

import java.util.Comparator;
import java.util.List;
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
                .filter(Attendance::isTardy)
                .count();
    }

    public boolean isTardyStackFull() {
        return countTardy() >= numberOfParticipants;
    }

    public void disableAttendances(final int disableSize) {
        final List<Attendance> filteredAttendances = values.stream()
                .filter(Attendance::isEnabled)
                .filter(Attendance::isTardy)
                .limit(disableSize)
                .sorted(Comparator.comparingLong(Attendance::getId))
                .collect(Collectors.toList());

        filteredAttendances.forEach(Attendance::disable);
    }

    private void validateSingleMeeting(final List<Attendance> value) {
        final long meetingCount = value.stream()
                .map(Attendance::getParticipant)
                .map(Participant::getMeeting)
                .distinct()
                .count();

        if (meetingCount > 1) {
            throw new IllegalArgumentException();
        }
    }
}
