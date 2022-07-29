package com.woowacourse.moragora.entity;

import com.woowacourse.moragora.exception.meeting.AttendanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public class ParticipantAttendances {

    private final List<Attendance> value;

    public ParticipantAttendances(final List<Attendance> value) {
        validateSingleParticipant(value);
        this.value = value;
    }

    public Attendance extractAttendanceByDate(final LocalDate date) {
        return value.stream()
                .filter(it -> it.isSameDate(date))
                .findAny()
                .orElseThrow(AttendanceNotFoundException::new);
    }

    public int countTardy(final boolean isOver, final LocalDate today) {
        final Stream<Attendance> attendances = value.stream()
                .filter(it -> it.isSameStatus(Status.TARDY));

        if (isOver) {
            return (int) attendances.count();
        }

        return (int) attendances.filter(attendance -> attendance.getAttendanceDate().isBefore(today))
                .count();
    }

    private void validateSingleParticipant(final List<Attendance> value) {
        final long participantCount = value.stream()
                .map(Attendance::getParticipant)
                .distinct()
                .count();

        if (participantCount > 1) {
            throw new IllegalArgumentException();
        }
    }
}
