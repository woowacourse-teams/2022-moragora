package com.woowacourse.moragora.entity;

import java.util.List;
import java.util.stream.Collectors;

public class MeetingAttendances {

    private final List<Attendance> values;

    public MeetingAttendances(final List<Attendance> values) {
        validateSingleMeeting(values);
        this.values = values;
    }

    public ParticipantAttendances extractAttendancesByParticipant(final Participant participant) {
        final List<Attendance> attendances = this.values.stream()
                .filter(attendance -> attendance.getParticipant().equals(participant))
                .collect(Collectors.toList());
        return new ParticipantAttendances(attendances);
    }

    public long extractProceedDate() {
        return values.stream()
                .map(Attendance::getAttendanceDate)
                .distinct()
                .count();
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
