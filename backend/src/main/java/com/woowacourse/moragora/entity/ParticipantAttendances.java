package com.woowacourse.moragora.entity;

import com.woowacourse.moragora.exception.meeting.AttendanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public class ParticipantAttendances {

    private final List<Attendance> values;

    public ParticipantAttendances(final List<Attendance> values) {
        validateSingleParticipant(values);
        this.values = values;
    }

    public Attendance extractAttendanceByDate(final LocalDate date) {
        return values.stream()
                .filter(it -> it.isSameDate(date))
                .findAny()
                .orElseThrow(AttendanceNotFoundException::new);
    }

    public int countTardy(final boolean isAttendanceClosed, final LocalDate today) {
        final Stream<Attendance> attendances = values.stream()
                .filter(Attendance::isEnabled)
                .filter(Attendance::isTardy);

        if (isAttendanceClosed) {
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
