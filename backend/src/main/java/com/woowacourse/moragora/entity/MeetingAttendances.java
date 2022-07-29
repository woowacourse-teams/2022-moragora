package com.woowacourse.moragora.entity;

import java.util.List;
import java.util.stream.Collectors;

public class MeetingAttendances {

    private final List<Attendance> value;

    public MeetingAttendances(final List<Attendance> value) {
        validateSingleMeeting(value);
        this.value = value;
    }

    public ParticipantAttendances extractAttendancesByParticipant(final Participant participant) {
        final List<Attendance> attendances = this.value.stream()
                .filter(attendance -> attendance.getParticipant().equals(participant))
                .collect(Collectors.toList());
        return new ParticipantAttendances(attendances);
    }

    public long extractProceedDate() {
        return value.stream()
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
