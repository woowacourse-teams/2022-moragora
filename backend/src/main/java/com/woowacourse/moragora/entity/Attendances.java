package com.woowacourse.moragora.entity;

import com.woowacourse.moragora.exception.meeting.AttendanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public class Attendances {

    private final List<Attendance> value;
    private final Participant participant;

    public Attendances(final List<Attendance> value, final Participant participant) {
        this.value = value;
        this.participant = participant;
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
}
