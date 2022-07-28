package com.woowacourse.moragora.entity;

import java.util.List;
import java.util.stream.Collectors;

public class AllAttendances {

    private final List<Attendance> attendances;

    public AllAttendances(final List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public Attendances extractAttendancesByParticipant(final Participant participant) {
        final List<Attendance> attendances = this.attendances.stream()
                .filter(attendance -> attendance.getParticipant().equals(participant))
                .collect(Collectors.toList());
        return new Attendances(attendances, participant);
    }

    public long extractProceedDate() {
        return attendances.stream()
                .map(Attendance::getAttendanceDate)
                .distinct()
                .count();
    }
}
